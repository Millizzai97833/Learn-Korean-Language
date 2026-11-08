package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppAd.AdMode
import com.startapp.sdk.adsbase.VideoListener
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener

/**
 * Manages loading and displaying Start.io Rewarded Video ads.
 * Ad requests are strictly deferred until StartIoSdkState confirms SDK readiness.
 */
class RewardedInterstitialAdManager(
    private val context: Context
) {
    private var rewardedAd: StartAppAd? = null
    private var isLoading = false
    private var isWaitingForSdk = false

    companion object {
        const val REWARD_AMOUNT = 150
    }

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
                Log.d(AdConfig.TAG_REWARDED, "SDK not ready yet. Queuing rewarded video request...")
                StartIoSdkState.runWhenReady {
                    isWaitingForSdk = false
                    loadAd()
                }
            }
            return
        }

        if (isLoading) {
            Log.d(AdConfig.TAG_REWARDED, "Rewarded video is already loading, skipping request.")
            return
        }

        if (rewardedAd != null) {
            Log.d(AdConfig.TAG_REWARDED, "Rewarded video is already cached and ready to show.")
            return
        }

        isLoading = true
        Log.i(AdConfig.TAG_REWARDED, "Requesting Start.io Rewarded Video (AdMode.REWARDED_VIDEO)...")

        val newAd = StartAppAd(context)

        try {
            newAd.loadAd(
                AdMode.REWARDED_VIDEO,
                object : AdEventListener {
                    override fun onReceiveAd(ad: Ad) {
                        rewardedAd = newAd
                        isLoading = false
                        Log.i(
                            AdConfig.TAG_REWARDED,
                            "Start.io Rewarded Video loaded successfully and ready to display"
                        )
                    }

                    override fun onFailedToReceiveAd(ad: Ad?) {
                        rewardedAd = null
                        isLoading = false
                        val errorMsg = ad?.errorMessage ?: "No ad fill or network error"
                        Log.w(
                            AdConfig.TAG_REWARDED,
                            "Start.io Rewarded Video failed to load: $errorMsg"
                        )
                    }
                }
            )
        } catch (e: Exception) {
            isLoading = false
            Log.e(AdConfig.TAG_REWARDED, "Exception while requesting Rewarded Video: ${e.message}", e)
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
        val ad = rewardedAd

        if (ad == null) {
            Log.w(AdConfig.TAG_REWARDED, "No rewarded video ad cached when show() called. Initiating reload.")
            loadAd()
            onAdNotReady()
            return
        }

        var rewardGranted = false
        var finished = false

        fun finish() {
            if (finished) return
            finished = true
            rewardedAd = null
            // Preload next rewarded ad in the background
            loadAd()
            onAdClosed()
        }

        try {
            ad.setVideoListener(object : VideoListener {
                override fun onVideoCompleted() {
                    if (!rewardGranted) {
                        rewardGranted = true
                        Log.i(
                            AdConfig.TAG_REWARDED,
                            "Rewarded video completed! Granting $REWARD_AMOUNT XP reward."
                        )
                        onRewardEarned("topik_bonus_unlock", REWARD_AMOUNT)
                    }
                }
            })

            ad.showAd(object : AdDisplayListener {
                override fun adDisplayed(ad: Ad) {
                    Log.i(AdConfig.TAG_REWARDED, "Rewarded video displayed on screen")
                }

                override fun adHidden(ad: Ad) {
                    Log.i(AdConfig.TAG_REWARDED, "Rewarded video closed by user")
                    finish()
                }

                override fun adClicked(ad: Ad) {
                    Log.d(AdConfig.TAG_REWARDED, "Rewarded video clicked by user")
                }

                override fun adNotDisplayed(ad: Ad) {
                    Log.e(AdConfig.TAG_REWARDED, "Rewarded video could not be displayed")
                    finish()
                }
            })
        } catch (e: Exception) {
            Log.e(AdConfig.TAG_REWARDED, "Exception while showing rewarded ad: ${e.message}", e)
            finish()
        }
    }
}
