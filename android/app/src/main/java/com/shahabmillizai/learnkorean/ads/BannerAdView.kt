
package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

enum class StartIoBannerSize {
    STANDARD,
    LARGE_MREC
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    bannerSize: StartIoBannerSize =
        StartIoBannerSize.STANDARD
) {
    if (!AdConfig.sdkReady) return

    val context = LocalContext.current
    val activity = remember(context) {
        context.findActivity()
    }

    val adSize = if (
        bannerSize == StartIoBannerSize.LARGE_MREC
    ) {
        AdSize.MEDIUM_RECTANGLE
    } else {
        AdSize.BANNER
    }

    val adHeight = if (
        bannerSize == StartIoBannerSize.LARGE_MREC
    ) {
        250.dp
    } else {
        50.dp
    }

    val adView = remember(activity, bannerSize) {
        AdView(activity ?: context).apply {
            adUnitId = AdConfig.bannerId()
            setAdSize(adSize)

            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.i(
                        AdConfig.TAG,
                        "Mediated banner loaded"
                    )
                }

                override fun onAdFailedToLoad(
                    error: LoadAdError
                ) {
                    Log.e(
                        AdConfig.TAG,
                        "Banner failed: ${error.message}"
                    )
                }

                override fun onAdImpression() {
                    Log.d(
                        AdConfig.TAG,
                        "Banner impression recorded"
                    )
                }
            }
        }
    }

    DisposableEffect(adView) {
        adView.loadAd(
            AdRequest.Builder().build()
        )

        onDispose {
            adView.destroy()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(adHeight + 12.dp)
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { adView },
            modifier = Modifier
                .fillMaxWidth()
                .height(adHeight)
        )
    }
}
