package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback

/**
 * Handles Rewarded Interstitial Ads:
 * - Grants bonus vocabulary, study hints, and XP points
 * - Honors user confirmation before playback
 * - Strictly verifies reward confirmation from Google SDK before unlocking content
 */
class RewardedInterstitialAdManager(private val context: Context) {

    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    private var isLoading = false

    companion object {
        private const val TAG = "RewardedInterstitial"
    }

    init {
        loadAd()
    }

    fun loadAd() {
        if (isLoading || rewardedInterstitialAd != null) return
        isLoading = true

        val adRequest = AdRequest.Builder().build()
        RewardedInterstitialAd.load(
            context,
            AdConfig.getRewardedInterstitialAdUnitId(),
            adRequest,
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    rewardedInterstitialAd = ad
                    isLoading = false
                    Log.d(TAG, "Rewarded Interstitial Ad loaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    rewardedInterstitialAd = null
                    isLoading = false
                    Log.w(TAG, "Rewarded Interstitial failed to load: ${loadAdError.message}")
                }
            }
        )
    }

    val isAdAvailable: Boolean
        get() = rewardedInterstitialAd != null

    /**
     * Shows the rewarded interstitial ad and invokes onRewardEarned only after SDK validation.
     */
    fun show(
        activity: Activity,
        onRewardEarned: (rewardType: String, amount: Int) -> Unit,
        onAdClosed: () -> Unit
    ) {
        val ad = rewardedInterstitialAd
        if (ad == null) {
            loadAd()
            onAdClosed()
            return
        }

        var rewardGranted = false

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedInterstitialAd = null
                loadAd()
                onAdClosed()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                rewardedInterstitialAd = null
                loadAd()
                onAdClosed()
            }
        }

        ad.show(activity) { rewardItem ->
            rewardGranted = true
            Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
            onRewardEarned(rewardItem.type, rewardItem.amount)
        }
    }
}
