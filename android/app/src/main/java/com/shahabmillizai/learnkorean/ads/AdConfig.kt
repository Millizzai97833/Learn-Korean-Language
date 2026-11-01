package com.shahabmillizai.learnkorean.ads

import android.content.Context
import com.shahabmillizai.learnkorean.BuildConfig

/**
 * AdMob Configuration for Learn Korean Language
 * Developer: Shahab Millizai
 *
 * Implements strict separation between Google's official Test Ad IDs (Debug mode)
 * and Production Real Ad Unit IDs (Release mode) to prevent policy violations.
 */
object AdConfig {
    // Official Real AdMob Application ID
    const val REAL_APP_ID = "ca-app-pub-2790436784008422~8320873870"

    // Real Production Ad Unit IDs provided for Shahab Millizai
    const val REAL_BANNER_ID = "ca-app-pub-2790436784008422/7007792206"
    const val REAL_INTERSTITIAL_ID = "ca-app-pub-2790436784008422/5419378368"
    const val REAL_REWARDED_INTERSTITIAL_ID = "ca-app-pub-2790436784008422/7366437749"
    const val REAL_APP_OPEN_ID = "ca-app-pub-2790436784008422/1048441408"
    const val REAL_NATIVE_ID = "ca-app-pub-2790436784008422/3681297429"

    // Google's Official Test Ad Unit IDs (Safe for development and testing)
    const val TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"
    const val TEST_REWARDED_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/5354046379"
    const val TEST_APP_OPEN_ID = "ca-app-pub-3940256099942544/9257395921"
    const val TEST_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110"

    // Toggle for testing inside the app settings
    var forceTestMode: Boolean = BuildConfig.DEBUG

    fun getBannerAdUnitId(): String {
        return if (forceTestMode || BuildConfig.DEBUG) TEST_BANNER_ID else REAL_BANNER_ID
    }

    fun getInterstitialAdUnitId(): String {
        return if (forceTestMode || BuildConfig.DEBUG) TEST_INTERSTITIAL_ID else REAL_INTERSTITIAL_ID
    }

    fun getRewardedInterstitialAdUnitId(): String {
        return if (forceTestMode || BuildConfig.DEBUG) TEST_REWARDED_INTERSTITIAL_ID else REAL_REWARDED_INTERSTITIAL_ID
    }

    fun getAppOpenAdUnitId(): String {
        return if (forceTestMode || BuildConfig.DEBUG) TEST_APP_OPEN_ID else REAL_APP_OPEN_ID
    }

    fun getNativeAdUnitId(): String {
        return if (forceTestMode || BuildConfig.DEBUG) TEST_NATIVE_ID else REAL_NATIVE_ID
    }
}
