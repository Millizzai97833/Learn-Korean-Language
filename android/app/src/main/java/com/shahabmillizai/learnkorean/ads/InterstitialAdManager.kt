package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener

/**
 * Manages loading and displaying Start.io Interstitial ads.
 * Displayed every 3 completed lessons outside of quiz flows.
 */
class InterstitialAdManager(private val context: Context) {

    private var interstitialAd: StartAppAd? = null
    private var isLoading = false
    private var isWaitingForSdk = false
    private var completedLessonCount = 0

    private val lessonThreshold = 3

    init {
        // Register load listener that fires ONLY after SDK initialization completes
        StartIoSdkState.runWhenReady {
            loadAd()
        }
    }

    fun loadAd() {
        if (!StartIoSdkState.isSdkReady()) {
            if (!isWaitingForSdk) {
                isWaitingForSdk = true
                Log.d(AdConfig.TAG_INTERSTITIAL, "SDK not ready yet. Queuing interstitial request...")
                StartIoSdkState.runWhenReady {
                    isWaitingForSdk = false
                    loadAd()
                }
            }
            return
        }

        if (isLoading) {
            Log.d(AdConfig.TAG_INTERSTITIAL, "Interstitial is already loading.")
            return
        }

        if (interstitialAd != null) {
            Log.d(AdConfig.TAG_INTERSTITIAL, "Interstitial is already cached.")
            return
        }

        isLoading = true
        Log.i(AdConfig.TAG_INTERSTITIAL, "Requesting Start.io Interstitial ad...")

        val newAd = StartAppAd(context)

        try {
            newAd.loadAd(object : AdEventListener {
                override fun onReceiveAd(ad: Ad) {
                    interstitialAd = newAd
                    isLoading = false
                    Log.i(AdConfig.TAG_INTERSTITIAL, "Start.io Interstitial ad loaded successfully")
                }

                override fun onFailedToReceiveAd(ad: Ad?) {
                    interstitialAd = null
                    isLoading = false
                    val errorMsg = ad?.errorMessage ?: "No ad fill or network error"
                    Log.w(AdConfig.TAG_INTERSTITIAL, "Start.io Interstitial failed to load: $errorMsg")
                }
            })
        } catch (e: Exception) {
            isLoading = false
            Log.e(AdConfig.TAG_INTERSTITIAL, "Exception while requesting Interstitial: ${e.message}", e)
        }
    }

    fun showIfAllowed(
        activity: Activity,
        isQuizActive: Boolean,
        onDismissed: () -> Unit
    ) {
        if (isQuizActive) {
            onDismissed()
            return
        }

        completedLessonCount++
        Log.d(
            AdConfig.TAG_INTERSTITIAL,
            "Lesson completed ($completedLessonCount / $lessonThreshold threshold)"
        )

        if (completedLessonCount < lessonThreshold) {
            onDismissed()
            return
        }

        val ad = interstitialAd

        if (ad == null) {
            Log.w(AdConfig.TAG_INTERSTITIAL, "Interstitial not ready at lesson threshold. Preloading...")
            loadAd()
            onDismissed()
            return
        }

        var finished = false

        fun finish() {
            if (finished) return
            finished = true
            interstitialAd = null
            completedLessonCount = 0
            // Preload next interstitial
            loadAd()
            onDismissed()
        }

        try {
            ad.showAd(object : AdDisplayListener {
                override fun adDisplayed(ad: Ad) {
                    Log.i(AdConfig.TAG_INTERSTITIAL, "Interstitial ad displayed on screen")
                }

                override fun adHidden(ad: Ad) {
                    Log.i(AdConfig.TAG_INTERSTITIAL, "Interstitial ad closed by user")
                    finish()
                }

                override fun adClicked(ad: Ad) {
                    Log.d(AdConfig.TAG_INTERSTITIAL, "Interstitial ad clicked")
                }

                override fun adNotDisplayed(ad: Ad) {
                    Log.e(AdConfig.TAG_INTERSTITIAL, "Interstitial ad could not be displayed")
                    finish()
                }
            })
        } catch (e: Exception) {
            Log.e(AdConfig.TAG_INTERSTITIAL, "Exception while showing interstitial: ${e.message}", e)
            finish()
        }
    }
}
