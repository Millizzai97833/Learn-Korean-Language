
package com.shahabmillizai.learnkorean

import android.app.Application
import android.util.Log
import com.shahabmillizai.learnkorean.ads.StartIoSdkState
import com.startapp.sdk.adsbase.StartAppSDK

class LearnKoreanApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            StartAppSDK.setTestAdsEnabled(true)
        }

        StartAppSDK.initParams(
            applicationContext,
            "210777317"
        )
            .setReturnAdsEnabled(false)
            .setCallback {
                Log.d("LearnKoreanApp", "Start.io SDK initialized and ready")
                StartIoSdkState.markReady()
            }
            .init()
    }
}
