
package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAdManager(
    private val context: Context
) {
    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var completedLessonCount = 0

    private val lessonThreshold = 3

    fun loadAd() {
        if (!AdConfig.sdkReady || isLoading || interstitialAd != null) return

        isLoading = true

        InterstitialAd.load(
            context,
            AdConfig.interstitialId(),
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                    Log.d(AdConfig.TAG, "Interstitial loaded")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                    Log.e(AdConfig.TAG, error.message)
                }
            }
        )
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

        if (activity.isFinishing || activity.isDestroyed) {
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

        ad.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    finish()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    Log.e(AdConfig.TAG, error.message)
                    finish()
                }
            }

        ad.show(activity)
    }
}
