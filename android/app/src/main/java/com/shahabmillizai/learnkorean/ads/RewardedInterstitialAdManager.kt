
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

class RewardedInterstitialAdManager(
    private val context: Context
) {
    private var rewardedAd: StartAppAd? = null
    private var isLoading = false
    private var isWaitingForSdk = false

    companion object {
        private const val TAG = "StartIoRewarded"
        private const val REWARD_AMOUNT = 150
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

        if (isLoading || rewardedAd != null) return

        isLoading = true
        Log.i(TAG, "Requesting rewarded video")

        val newAd = StartAppAd(context)

        try {
            newAd.loadAd(
                AdMode.REWARDED_VIDEO,
                object : AdEventListener {
                    override fun onReceiveAd(ad: Ad) {
                        rewardedAd = newAd
                        isLoading = false
                        Log.i(TAG, "Rewarded video loaded successfully")
                    }

                    override fun onFailedToReceiveAd(ad: Ad?) {
                        rewardedAd = null
                        isLoading = false
                        Log.e(TAG, "Rewarded video failed to load: $ad")
                    }
                }
            )
        } catch (e: Exception) {
            isLoading = false
            Log.e(TAG, "Rewarded load exception", e)
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
            Log.w(TAG, "No rewarded video available. Requesting another ad.")
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
            loadAd()
            onAdClosed()
        }

        try {
            ad.setVideoListener(object : VideoListener {
                override fun onVideoCompleted() {
                    if (!rewardGranted) {
                        rewardGranted = true
                        Log.i(TAG, "Rewarded video completed")

                        onRewardEarned(
                            "video_completed",
                            REWARD_AMOUNT
                        )
                    }
                }
            })

            ad.showAd(object : AdDisplayListener {
                override fun adDisplayed(ad: Ad) {
                    Log.i(TAG, "Rewarded video displayed")
                }

                override fun adHidden(ad: Ad) {
                    Log.i(TAG, "Rewarded video closed")
                    finish()
                }

                override fun adClicked(ad: Ad) {
                    Log.d(TAG, "Rewarded video clicked")
                }

                override fun adNotDisplayed(ad: Ad) {
                    Log.e(TAG, "Rewarded video could not be displayed")
                    finish()
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Rewarded display exception", e)
            finish()
        }
    }
}
