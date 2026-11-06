package com.shahabmillizai.learnkorean.ads

import com.shahabmillizai.learnkorean.BuildConfig

object AdConfig {

    const val START_IO_APP_ID = "210777317"

    // Debug builds use Start.io test ads.
    var forceTestMode: Boolean = BuildConfig.DEBUG
}
