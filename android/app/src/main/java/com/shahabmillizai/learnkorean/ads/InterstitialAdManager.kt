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

/**
 * Manages Interstitial Ads safely:
 * - Preloads in background
 * - Strict frequency capping (only shows after specified completed lessons)
 * - Guaranteed NEVER to interrupt active quizzes or conversations
 */
class InterstitialAdManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var completedLessonCount = 0
    private val lessonThreshold = 3 // Show at most once every 3 completed lessons

    companion object {
        private const val TAG = "InterstitialAdManager"
    }

    init {
        loadAd()
    }

    fun loadAd() {
        if (isLoading || interstitialAd != null) return
        isLoading = true

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            AdConfig.getInterstitialAdUnitId(),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                    Log.d(TAG, "Interstitial Ad successfully loaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                    Log.w(TAG, "Interstitial Ad failed to load: ${loadAdError.message}")
                }
            }
        )
    }

    /**
     * Call when a lesson is completed.
     * Evaluates natural transition & frequency capping before presenting.
     */
    fun showIfAllowed(activity: Activity, isQuizActive: Boolean, onDismissed: () -> Unit) {
        // Strict policy: Never interrupt active exercises or tests
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
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    completedLessonCount = 0
                    loadAd() // Preload next
                    onDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    loadAd()
                    onDismissed()
                }
            }
            ad.show(activity)
        } else {
            // Ad not ready yet, continue seamlessly without blocking user
            loadAd()
            onDismissed()
        }
    }
}
