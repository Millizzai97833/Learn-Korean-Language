package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.VideoListener
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener

class RewardedInterstitialAdManager(
    private val context: Context
) {
    private var rewardedAd: StartAppAd? = null
    private var isLoading = false

    companion object {
        private const val TAG = "StartIoRewarded"
    }

    init {
        loadAd()
    }

    fun loadAd() {
        if (isLoading || rewardedAd != null) return

        isLoading = true
        val newAd = StartAppAd(context.applicationContext)

        newAd.loadAd(
            StartAppAd.AdMode.REWARDED_VIDEO,
            object : AdEventListener {
                override fun onReceiveAd(ad: Ad) {
                    rewardedAd = newAd
                    isLoading = false
                    Log.d(TAG, "Start.io rewarded video loaded")
                }

                override fun onFailedToReceiveAd(ad: Ad?) {
                    rewardedAd = null
                    isLoading = false
                    Log.w(TAG, "Start.io rewarded video failed to load")
                }
            }
        )
    }

    val isAdAvailable: Boolean
        get() = rewardedAd != null

    fun show(
        activity: Activity,
        onRewardEarned: (rewardType: String, amount: Int) -> Unit,
        onAdClosed: () -> Unit
    ) {
        val ad = rewardedAd

        if (ad == null) {
            loadAd()
            onAdClosed()
            return
        }

        var rewardGranted = false
        var closeCallbackUsed = false

        fun finishAd() {
            if (closeCallbackUsed) return
            closeCallbackUsed = true
            rewardedAd = null
            loadAd()
            onAdClosed()
        }

        ad.setVideoListener(object : VideoListener {
            override fun onVideoCompleted() {
                if (!rewardGranted) {
                    rewardGranted = true
                    Log.d(TAG, "Rewarded video completed")
                    onRewardEarned("video_completed", 150)
                }
            }
        })

        ad.showAd(object : AdDisplayListener {
            override fun adDisplayed(ad: Ad) {
                Log.d(TAG, "Start.io rewarded video displayed")
            }

            override fun adHidden(ad: Ad) {
                finishAd()
            }

            override fun adClicked(ad: Ad) {
                Log.d(TAG, "Start.io rewarded video clicked")
            }

            override fun adNotDisplayed(ad: Ad) {
                finishAd()
            }
        })
    }
}
