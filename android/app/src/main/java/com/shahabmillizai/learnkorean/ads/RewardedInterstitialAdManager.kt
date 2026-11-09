
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

class RewardedInterstitialAdManager(
    private val context: Context
) {
    private var rewardedAd: RewardedInterstitialAd? = null
    private var isLoading = false

    private data class PendingReward(
        val activity: Activity,
        val onRewardEarned: (String, Int) -> Unit,
        val onAdClosed: () -> Unit,
        val onAdNotReady: () -> Unit
    )

    private var pendingReward: PendingReward? = null

    companion object {
        const val REWARD_AMOUNT = 150
    }

    val isAdAvailable: Boolean
        get() = rewardedAd != null

    val isAdLoading: Boolean
        get() = isLoading

    fun loadAd() {
        if (!AdConfig.sdkReady || isLoading || rewardedAd != null) return

        isLoading = true

        RewardedInterstitialAd.load(
            context,
            AdConfig.rewardedId(),
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    rewardedAd = ad
                    isLoading = false

                    val request = pendingReward
                    if (request != null) {
                        pendingReward = null
                        displayAd(request, ad)
                    }

                    Log.d(AdConfig.TAG, "Rewarded interstitial loaded")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    isLoading = false

                    Log.e(AdConfig.TAG, error.message)

                    val request = pendingReward
                    pendingReward = null
                    request?.onAdNotReady?.invoke()
                }
            }
        )
    }

    fun show(
        activity: Activity,
        onRewardEarned: (rewardType: String, amount: Int) -> Unit,
        onAdClosed: () -> Unit,
        onAdNotReady: () -> Unit
    ) {
        if (!AdConfig.sdkReady ||
            activity.isFinishing ||
            activity.isDestroyed
        ) {
            onAdNotReady()
            return
        }

        val request = PendingReward(
            activity,
            onRewardEarned,
            onAdClosed,
            onAdNotReady
        )

        val ad = rewardedAd

        if (ad != null) {
            rewardedAd = null
            displayAd(request, ad)
            return
        }

        if (pendingReward != null) return

        pendingReward = request
        loadAd()
    }

    private fun displayAd(
        request: PendingReward,
        ad: RewardedInterstitialAd
    ) {
        val activity = request.activity

        if (activity.isFinishing || activity.isDestroyed) {
            request.onAdNotReady()
            loadAd()
            return
        }

        var finished = false

        fun finish(failed: Boolean) {
            if (finished) return
            finished = true

            if (failed) {
                request.onAdNotReady()
            } else {
                request.onAdClosed()
            }

            loadAd()
        }

        ad.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    finish(false)
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    Log.e(AdConfig.TAG, error.message)
                    finish(true)
                }
            }

        ad.show(activity) {
            request.onRewardEarned(
                "topik_bonus_unlock",
                REWARD_AMOUNT
            )
        }
    }
}
