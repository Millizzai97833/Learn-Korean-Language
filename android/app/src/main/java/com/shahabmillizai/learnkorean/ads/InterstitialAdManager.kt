package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener

class InterstitialAdManager(private val context: Context) {

    private var interstitialAd: StartAppAd? = null
    private var isLoading = false
    private var isWaitingForSdk = false
    private var completedLessonCount = 0

    private val lessonThreshold = 3

    companion object {
        private const val TAG = "StartIoInterstitial"
    }

    init {
        loadAd()
    }

    fun loadAd() {
        if (!StartIoSdkState.isSdkReady()) {
            if (!isWaitingForSdk) {
                isWaitingForSdk = true
                StartIoSdkState.runWhenReady {
                    isWaitingForSdk = false
                    loadAd()
                }
            }
            return
        }

        if (isLoading || interstitialAd != null) return

        isLoading = true
        Log.d(TAG, "Requesting interstitial")

        // Use the Activity context, not applicationContext.
        val newAd = StartAppAd(context)

        try {
            newAd.loadAd(object : AdEventListener {

                override fun onReceiveAd(ad: Ad) {
                    interstitialAd = newAd
                    isLoading = false
                    Log.i(TAG, "Interstitial loaded")
                }

                override fun onFailedToReceiveAd(ad: Ad?) {
                    interstitialAd = null
                    isLoading = false
                    Log.e(TAG, "Interstitial failed: $ad")
                }
            })
        } catch (e: Exception) {
            isLoading = false
            Log.e(TAG, "Interstitial request exception", e)
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

        if (completedLessonCount < lessonThreshold) {
            onDismissed()
            return
        }

        val ad = interstitialAd

        if (ad == null) {
            Log.w(TAG, "Interstitial not ready")
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
            loadAd()
            onDismissed()
        }

        try {
            ad.showAd(object : AdDisplayListener {

                override fun adDisplayed(ad: Ad) {
                    Log.i(TAG, "Interstitial displayed")
                }

                override fun adHidden(ad: Ad) {
                    finish()
                }

                override fun adClicked(ad: Ad) {
                    Log.d(TAG, "Interstitial clicked")
                }

                override fun adNotDisplayed(ad: Ad) {
                    Log.e(TAG, "Interstitial not displayed")
                    finish()
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Interstitial display exception", e)
            finish()
        }
    }
}
