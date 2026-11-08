
package com.shahabmillizai.learnkorean.ads

import com.shahabmillizai.learnkorean.BuildConfig

object AdConfig {

    // New Start.io App ID
    const val START_IO_APP_ID = "210896026"

    const val TAG_SDK = "StartIoSdk"
    const val TAG_BANNER = "StartIoBanner"
    const val TAG_REWARDED = "StartIoRewarded"
    const val TAG_INTERSTITIAL = "StartIoInterstitial"

    const val MREC_WIDTH_DP = 300
    const val MREC_HEIGHT_DP = 250

    const val STANDARD_BANNER_HEIGHT_DP = 50

    var forceTestMode: Boolean = BuildConfig.DEBUG
}
