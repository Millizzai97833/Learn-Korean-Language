package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener

class InterstitialAdManager(
    private val context: Context
) {
    private var interstitialAd: StartAppAd? = null
    private var isLoading = false
    private var completedLessonCount = 0

    private val lessonThreshold = 3

    companion object {
        private const val TAG = "StartIoInterstitial"
    }

    init {
        loadAd()
    }

    fun loadAd() {
        if (isLoading || interstitialAd != null) return

        isLoading = true
        val newAd = StartAppAd(context.applicationContext)

        newAd.loadAd(object : AdEventListener {
            override fun onReceiveAd(ad: Ad) {
                interstitialAd = newAd
                isLoading = false
                Log.d(TAG, "Start.io interstitial loaded")
            }

            override fun onFailedToReceiveAd(ad: Ad?) {
                interstitialAd = null
                isLoading = false
                Log.w(TAG, "Start.io interstitial failed to load")
            }
        })
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
            loadAd()
            onDismissed()
            return
        }

        var callbackUsed = false

        fun finishAd() {
            if (callbackUsed) return
            callbackUsed = true
            interstitialAd = null
            completedLessonCount = 0
            loadAd()
            onDismissed()
        }

        ad.showAd(object : AdDisplayListener {
            override fun adDisplayed(ad: Ad) {
                Log.d(TAG, "Start.io interstitial displayed")
            }

            override fun adHidden(ad: Ad) {
                finishAd()
            }

            override fun adClicked(ad: Ad) {
                Log.d(TAG, "Start.io interstitial clicked")
            }

            override fun adNotDisplayed(ad: Ad) {
                finishAd()
            }
        })
    }
}
