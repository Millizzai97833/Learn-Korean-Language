package com.shahabmillizai.learnkorean.ads

import com.shahabmillizai.learnkorean.BuildConfig

object AdConfig {

    /**
     * Start.io Application ID registered for Learn Korean Language
     * (com.shahabmillizai.learnkorean).
     */
    const val START_IO_APP_ID = "210777317"

    // Logcat Tags for filtering ad events
    const val TAG_SDK = "StartIoSdk"
    const val TAG_BANNER = "StartIoBanner"
    const val TAG_REWARDED = "StartIoRewarded"
    const val TAG_INTERSTITIAL = "StartIoInterstitial"

    // Big Banner (Medium Rectangle - MREC) standard dimensions
    const val MREC_WIDTH_DP = 300
    const val MREC_HEIGHT_DP = 250

    // Standard Banner height
    const val STANDARD_BANNER_HEIGHT_DP = 50

    // Debug builds enable Start.io test ads for safe development
    var forceTestMode: Boolean = BuildConfig.DEBUG
}
