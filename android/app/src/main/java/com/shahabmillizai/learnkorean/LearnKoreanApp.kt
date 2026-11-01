package com.shahabmillizai.learnkorean

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.shahabmillizai.learnkorean.ads.AdConfig
import com.shahabmillizai.learnkorean.ads.AppOpenAdManager

/**
 * Main Application Class:
 * - Initializes Google Mobile Ads SDK asynchronously on startup
 * - Registers AppOpenAdManager for seamless foreground transition ads
 * - Sets test device configuration during development
 */
class LearnKoreanApp : Application() {

    lateinit var appOpenAdManager: AppOpenAdManager
        private set

    override fun onCreate() {
        super.onCreate()

        // Initialize Google Mobile Ads SDK
        MobileAds.initialize(this) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for ((adapterClass, status) in statusMap) {
                Log.d("LearnKoreanApp", "Adapter: $adapterClass, State: ${status.initializationState}")
            }
        }

        // Configure test mode if running in debug
        if (BuildConfig.DEBUG) {
            val configuration = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf(RequestConfiguration.DEVICE_ID_EMULATOR))
                .build()
            MobileAds.setRequestConfiguration(configuration)
        }

        // Initialize App Open Ad Manager for app launch & return moments
        appOpenAdManager = AppOpenAdManager(this)
    }
}
