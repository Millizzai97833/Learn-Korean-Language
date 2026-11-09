
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
    context: Context
) {
    private val appContext = context.applicationContext

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
        if (
            !AdConfig.sdkReady ||
            isLoading ||
            rewardedAd != null
        ) return

        isLoading = true

        RewardedInterstitialAd.load(
            appContext,
            AdConfig.rewardedId(),
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {

                override fun onAdLoaded(
                    ad: RewardedInterstitialAd
                ) {
                    rewardedAd = ad
                    isLoading = false

                    Log.i(
                        AdConfig.TAG,
                        "Rewarded interstitial loaded"
                    )

                    val request = pendingReward

                    if (request != null) {
                        pendingReward = null
                        rewardedAd = null
                        displayAd(request, ad)
                    }
                }

                override fun onAdFailedToLoad(
                    error: LoadAdError
                ) {
                    rewardedAd = null
                    isLoading = false

                    Log.e(
                        AdConfig.TAG,
                        "Rewarded load failed: ${error.message}"
                    )

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
        if (
            !AdConfig.sdkReady ||
            activity.isFinishing ||
            activity.isDestroyed
        ) {
            onAdNotReady()
            return
        }

        if (pendingReward != null) {
            onAdNotReady()
            return
        }

        val request = PendingReward(
            activity = activity,
            onRewardEarned = onRewardEarned,
            onAdClosed = onAdClosed,
            onAdNotReady = onAdNotReady
        )

        val ad = rewardedAd

        if (ad != null) {
            rewardedAd = null
            displayAd(request, ad)
            return
        }

        pendingReward = request
        loadAd()
    }

    private fun displayAd(
        request: PendingReward,
        ad: RewardedInterstitialAd
    ) {
        val activity = request.activity

        if (
            activity.isFinishing ||
            activity.isDestroyed
        ) {
            request.onAdNotReady()
            loadAd()
            return
        }

        var finished = false
        var rewardEarned = false

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

                override fun onAdFailedToShowFullScreenContent(
                    error: AdError
                ) {
                    Log.e(
                        AdConfig.TAG,
                        "Rewarded display failed: ${error.message}"
                    )
                    finish(true)
                }
            }

        try {
            ad.show(activity) {
                if (!rewardEarned) {
                    rewardEarned = true

                    request.onRewardEarned(
                        "topik_bonus_unlock",
                        REWARD_AMOUNT
                    )
                }
            }
        } catch (exception: Exception) {
            Log.e(
                AdConfig.TAG,
                "Rewarded ad exception",
                exception
            )
            finish(true)
        }
    }
}
