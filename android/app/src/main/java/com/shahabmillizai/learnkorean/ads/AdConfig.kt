
package com.shahabmillizai.learnkorean.ads

object AdConfig {

    // Start.io
    const val START_IO_APP_ID = "210777317"

    // Existing AdMob configuration
    const val ADMOB_APP_ID =
        "ca-app-pub-2790436784008422~8320873870"

    const val BANNER_ID =
        "ca-app-pub-2790436784008422/7007792206"

    const val INTERSTITIAL_ID =
        "ca-app-pub-2790436784008422/5419378368"

    // Yandex dashboard app reference
    const val YANDEX_APP_ID = "20375564"

    // Your actual Yandex rewarded ad placement
    const val YANDEX_REWARDED_ID =
        "R-M-20375564-1"

    const val TAG = "LearnKoreanAds"

    // AdMob initialization status
    @Volatile
    var sdkReady = false

    // Yandex initialization status
    @Volatile
    var yandexSdkReady = false

    fun bannerId(): String = BANNER_ID

    fun interstitialId(): String = INTERSTITIAL_ID

    fun rewardedId(): String = YANDEX_REWARDED_ID
}
