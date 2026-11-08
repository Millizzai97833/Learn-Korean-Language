
package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppAd.AdMode
import com.startapp.sdk.adsbase.VideoListener
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener

class RewardedInterstitialAdManager(
    private val context: Context
) {
    private var rewardedAd: StartAppAd? = null
    private var isLoading = false
    private var isWaitingForSdk = false

    private val mainHandler = Handler(Looper.getMainLooper())

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

    init {
        StartIoSdkState.runWhenReady {
            loadAd()
        }
    }

    fun loadAd() {
        if (!StartIoSdkState.isSdkReady()) {
            if (StartIoSdkState.isSdkFailed()) {
                Log.e(
                    AdConfig.TAG_REWARDED,
                    "Cannot load rewarded ad: Start.io SDK initialization failed."
                )
                notifyPendingAdUnavailable()
                return
            }

            if (!isWaitingForSdk) {
                isWaitingForSdk = true

                Log.d(
                    AdConfig.TAG_REWARDED,
                    "Waiting for Start.io SDK before loading rewarded video."
                )

                StartIoSdkState.runWhenReady {
                    isWaitingForSdk = false
                    loadAd()
                }
            }
            return
        }

        if (isLoading || rewardedAd != null) {
            return
        }

        isLoading = true

        Log.i(
            AdConfig.TAG_REWARDED,
            "Requesting Start.io rewarded video."
        )

        val newAd = StartAppAd(context)

        try {
            newAd.loadAd(
                AdMode.REWARDED_VIDEO,
                object : AdEventListener {

                    override fun onReceiveAd(ad: Ad) {
                        mainHandler.post {
                            isLoading = false
                            rewardedAd = newAd

                            Log.i(
                                AdConfig.TAG_REWARDED,
                                "Rewarded video loaded successfully."
                            )

                            val request = pendingReward
                            if (request != null) {
                                pendingReward = null
                                displayRewardedAd(request, newAd)
                            }
                        }
                    }

                    override fun onFailedToReceiveAd(ad: Ad?) {
                        mainHandler.post {
                            isLoading = false
                            rewardedAd = null

                            val errorMessage =
                                ad?.errorMessage ?: "No ad fill or network error"

                            Log.e(
                                AdConfig.TAG_REWARDED,
                                "Rewarded video failed to load: $errorMessage"
                            )

                            notifyPendingAdUnavailable()
                        }
                    }
                }
            )
        } catch (e: Exception) {
            isLoading = false

            Log.e(
                AdConfig.TAG_REWARDED,
                "Exception while loading rewarded video.",
                e
            )

            notifyPendingAdUnavailable()
        }
    }

    val isAdAvailable: Boolean
        get() = rewardedAd != null

    val isAdLoading: Boolean
        get() = isLoading

    fun show(
        activity: Activity,
        onRewardEarned: (rewardType: String, amount: Int) -> Unit,
        onAdClosed: () -> Unit,
        onAdNotReady: () -> Unit
    ) {
        if (activity.isFinishing || activity.isDestroyed) {
            onAdNotReady()
            return
        }

        val request = PendingReward(
            activity = activity,
            onRewardEarned = onRewardEarned,
            onAdClosed = onAdClosed,
            onAdNotReady = onAdNotReady
        )

        val availableAd = rewardedAd

        if (availableAd != null) {
            rewardedAd = null
            displayRewardedAd(request, availableAd)
            return
        }

        if (pendingReward != null) {
            Log.w(
                AdConfig.TAG_REWARDED,
                "A rewarded ad request is already waiting."
            )
            return
        }

        pendingReward = request

        Log.i(
            AdConfig.TAG_REWARDED,
            "No cached rewarded ad. Waiting for an ad to load before displaying."
        )

        // If an ad is already loading, its callback will display it.
        // Otherwise, start a new request.
        loadAd()
    }

    private fun displayRewardedAd(
        request: PendingReward,
        ad: StartAppAd
    ) {
        val activity = request.activity

        if (activity.isFinishing || activity.isDestroyed) {
            Log.w(
                AdConfig.TAG_REWARDED,
                "Activity is no longer available. Cannot display rewarded video."
            )
            request.onAdNotReady()
            loadAd()
            return
        }

        var rewardGranted = false
        var finished = false

        fun finish() {
            if (finished) return
            finished = true

            Log.d(
                AdConfig.TAG_REWARDED,
                "Rewarded ad finished. Preparing the next ad."
            )

            loadAd()
            request.onAdClosed()
        }

        try {
            ad.setVideoListener(object : VideoListener {
                override fun onVideoCompleted() {
                    if (!rewardGranted) {
                        rewardGranted = true

                        Log.i(
                            AdConfig.TAG_REWARDED,
                            "Video completed. Granting $REWARD_AMOUNT XP."
                        )

                        request.onRewardEarned(
                            "topik_bonus_unlock",
                            REWARD_AMOUNT
                        )
                    }
                }
            })

            ad.showAd(object : AdDisplayListener {

                override fun adDisplayed(ad: Ad) {
                    Log.i(
                        AdConfig.TAG_REWARDED,
                        "Rewarded video displayed successfully."
                    )
                }

                override fun adHidden(ad: Ad) {
                    Log.i(
                        AdConfig.TAG_REWARDED,
                        "Rewarded video closed."
                    )
                    finish()
                }

                override fun adClicked(ad: Ad) {
                    Log.d(
                        AdConfig.TAG_REWARDED,
                        "Rewarded video clicked."
                    )
                }

                override fun adNotDisplayed(ad: Ad) {
                    Log.e(
                        AdConfig.TAG_REWARDED,
                        "Rewarded video could not be displayed."
                    )
                    finish()
                }
            })
        } catch (e: Exception) {
            Log.e(
                AdConfig.TAG_REWARDED,
                "Exception while displaying rewarded video.",
                e
            )
            finish()
        }
    }

    private fun notifyPendingAdUnavailable() {
        val request = pendingReward ?: return
        pendingReward = null

        mainHandler.post {
            Log.w(
                AdConfig.TAG_REWARDED,
                "No rewarded video is available for the pending request."
            )
            request.onAdNotReady()
        }
    }
}
