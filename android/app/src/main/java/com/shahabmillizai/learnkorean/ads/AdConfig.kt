
package com.shahabmillizai.learnkorean.ads

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

    const val TAG = "LearnKoreanAds"

    @Volatile
    var sdkReady = false

    fun bannerId() = BANNER_ID

    fun interstitialId() = INTERSTITIAL_ID

    fun rewardedId() = REWARDED_INTERSTITIAL_ID
}
