
package com.shahabmillizai.learnkorean.ads

import com.shahabmillizai.learnkorean.BuildConfig

object AdConfig {
    const val START_IO_APP_ID = "210777317"

    const val ADMOB_APP_ID =
        "ca-app-pub-2790436784008422~8320873870"

    const val BANNER_ID =
        "ca-app-pub-2790436784008422/7007792206"

    const val INTERSTITIAL_ID =
        "ca-app-pub-2790436784008422/5419378368"

    const val REWARDED_INTERSTITIAL_ID =
        "ca-app-pub-2790436784008422/7366437749"

    const val TEST_BANNER_ID =
        "ca-app-pub-3940256099942544/6300978111"

    const val TEST_INTERSTITIAL_ID =
        "ca-app-pub-3940256099942544/1033173712"

    const val TEST_REWARDED_INTERSTITIAL_ID =
        "ca-app-pub-3940256099942544/5354046379"

    const val TAG = "LearnKoreanAds"

    @Volatile
    var sdkReady = false

    fun bannerId() =
        if (BuildConfig.DEBUG) TEST_BANNER_ID else BANNER_ID

    fun interstitialId() =
        if (BuildConfig.DEBUG) TEST_INTERSTITIAL_ID else INTERSTITIAL_ID

    fun rewardedId() =
        if (BuildConfig.DEBUG) TEST_REWARDED_INTERSTITIAL_ID
        else REWARDED_INTERSTITIAL_ID
}
