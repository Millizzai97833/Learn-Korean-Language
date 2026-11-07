package com.shahabmillizai.learnkorean

import android.app.Application
import android.util.Log
import com.shahabmillizai.learnkorean.ads.AdConfig
import com.shahabmillizai.learnkorean.ads.StartIoSdkState
import com.startapp.sdk.adsbase.StartAppSDK

class LearnKoreanApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            StartAppSDK.setTestAdsEnabled(true)
        }

        Log.i("LearnKoreanApp", "Initializing Start.io with App ID ${AdConfig.START_IO_APP_ID}")

        StartAppSDK.initParams(
            applicationContext,
            AdConfig.START_IO_APP_ID
        )
            .setReturnAdsEnabled(false)
            .setCallback {
                Log.i("LearnKoreanApp", "Start.io SDK initialized and ready")
                StartIoSdkState.markReady()
            }
            .init()
    }
}
