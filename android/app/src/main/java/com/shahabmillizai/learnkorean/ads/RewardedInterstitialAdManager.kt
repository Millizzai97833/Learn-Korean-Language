
package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import android.util.Log

import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader

class RewardedInterstitialAdManager(context: Context) {

    private val appContext = context.applicationContext

    private var rewardedAdLoader: RewardedAdLoader? = null
    private var rewardedAd: RewardedAd? = null

    private var isLoading = false
    private var isShowing = false
    private var destroyed = false

    private data class PendingReward(
        val activity: Activity,
        val onRewardEarned: (String, Int) -> Unit,
        val onAdClosed: () -> Unit,
        val onAdNotReady: () -> Unit
    )

    private var pendingReward: PendingReward? = null

    companion object {
        const val REWARD_AMOUNT = 150
        private const val REWARD_TYPE = "topik_bonus_unlock"
    }

    val isAdAvailable: Boolean
        get() = rewardedAd != null

    val isAdLoading: Boolean
        get() = isLoading

    fun loadAd() {
        if (
            destroyed ||
            !AdConfig.yandexSdkReady ||
            isLoading ||
            rewardedAd != null
        ) {
            return
        }

        isLoading = true

        try {
            if (rewardedAdLoader == null) {
                rewardedAdLoader = RewardedAdLoader(appContext)
            }

            val request = AdRequest.Builder(
                AdConfig.rewardedId()
            ).build()

            rewardedAdLoader?.loadAd(
                request,
                object : RewardedAdLoadListener {

                    override fun onAdLoaded(ad: RewardedAd) {
                        isLoading = false

                        if (destroyed) {
                            ad.setAdEventListener(null)
                            return
                        }

                        rewardedAd = ad

                        Log.i(
                            AdConfig.TAG,
                            "Yandex rewarded ad loaded successfully"
                        )

                        val requestPending = pendingReward

                        if (requestPending != null) {
                            pendingReward = null
                            rewardedAd = null
                            displayAd(requestPending, ad)
                        }
                    }

                    override fun onAdFailedToLoad(
                        error: AdRequestError
                    ) {
                        isLoading = false
                        rewardedAd = null

                        Log.e(
                            AdConfig.TAG,
                            "Yandex rewarded load failed: $error"
                        )

                        val requestPending = pendingReward
                        pendingReward = null

                        requestPending?.onAdNotReady?.invoke()

                        // Do not immediately retry after a load failure.
                    }
                }
            )

        } catch (exception: Exception) {
            isLoading = false

            Log.e(
                AdConfig.TAG,
                "Yandex rewarded loading exception",
                exception
            )

            val requestPending = pendingReward
            pendingReward = null

            requestPending?.onAdNotReady?.invoke()
        }
    }

    fun show(
        activity: Activity,
        onRewardEarned: (rewardType: String, amount: Int) -> Unit,
        onAdClosed: () -> Unit,
        onAdNotReady: () -> Unit
    ) {
        if (
            destroyed ||
            !AdConfig.yandexSdkReady ||
            activity.isFinishing ||
            activity.isDestroyed
        ) {
            onAdNotReady()
            return
        }

        if (isShowing || pendingReward != null) {
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
        } else {
            pendingReward = request
            loadAd()
        }
    }

    private fun displayAd(
        request: PendingReward,
        ad: RewardedAd
    ) {
        val activity = request.activity

        if (
            destroyed ||
            activity.isFinishing ||
            activity.isDestroyed
        ) {
            ad.setAdEventListener(null)
            request.onAdNotReady()
            loadAd()
            return
        }

        var finished = false
        var rewardEarned = false

        isShowing = true

        fun finish(failed: Boolean) {
            if (finished) return

            finished = true
            isShowing = false

            ad.setAdEventListener(null)

            if (failed) {
                request.onAdNotReady()
            } else {
                request.onAdClosed()
            }

            loadAd()
        }

        ad.setAdEventListener(
            object : RewardedAdEventListener {

                override fun onAdShown() {
                    Log.i(
                        AdConfig.TAG,
                        "Yandex rewarded ad displayed"
                    )
                }

                override fun onAdFailedToShow(adError: AdError) {
                    Log.e(
                        AdConfig.TAG,
                        "Yandex rewarded display failed: $adError"
                    )

                    finish(true)
                }

                override fun onAdDismissed() {
                    Log.i(
                        AdConfig.TAG,
                        "Yandex rewarded ad dismissed"
                    )

                    finish(false)
                }

                override fun onAdClicked() {
                    Log.i(
                        AdConfig.TAG,
                        "Yandex rewarded ad clicked"
                    )
                }

                override fun onAdImpression(
                    impressionData: ImpressionData?
                ) {
                    Log.i(
                        AdConfig.TAG,
                        "Yandex rewarded impression recorded"
                    )
                }

                override fun onRewarded(reward: Reward) {
                    if (!rewardEarned) {
                        rewardEarned = true

                        Log.i(
                            AdConfig.TAG,
                            "Yandex reward confirmed"
                        )

                        request.onRewardEarned(
                            REWARD_TYPE,
                            REWARD_AMOUNT
                        )
                    }
                }
            }
        )

        try {
            ad.show(activity)
        } catch (exception: Exception) {
            Log.e(
                AdConfig.TAG,
                "Yandex rewarded show exception",
                exception
            )

            finish(true)
        }
    }

    fun destroy() {
        if (destroyed) return

        destroyed = true
        isLoading = false
        isShowing = false

        pendingReward = null

        rewardedAd?.setAdEventListener(null)
        rewardedAd = null

        rewardedAdLoader = null
    }
}
