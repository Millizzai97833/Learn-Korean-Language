package com.shahabmillizai.learnkorean

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.shahabmillizai.learnkorean.ads.AppOpenAdManager

class LearnKoreanApp : Application() {

    lateinit var appOpenAdManager: AppOpenAdManager
        private set

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            val configuration = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("EMULATOR"))
                .build()

            MobileAds.setRequestConfiguration(configuration)
        }

        MobileAds.initialize(this) { initializationStatus ->
            for ((adapterClass, status) in initializationStatus.adapterStatusMap) {
                Log.d(
                    "LearnKoreanApp",
                    "Adapter: $adapterClass, State: ${status.initializationState}"
                )
            }
        }

        appOpenAdManager = AppOpenAdManager(this)
    }
}
