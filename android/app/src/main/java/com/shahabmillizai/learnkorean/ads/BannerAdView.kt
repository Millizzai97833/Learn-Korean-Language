
package com.shahabmillizai.learnkorean.ads

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

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    bannerSize: StartIoBannerSize = StartIoBannerSize.STANDARD
) {
    val context = LocalContext.current

    val adSize = if (bannerSize == StartIoBannerSize.LARGE_MREC) {
        AdSize.MEDIUM_RECTANGLE
    } else {
        AdSize.BANNER
    }

    val height = if (bannerSize == StartIoBannerSize.LARGE_MREC) {
        250.dp
    } else {
        50.dp
    }

    val adView = remember(bannerSize) {
        AdView(context).apply {
            adUnitId = AdConfig.bannerId()
            setAdSize(adSize)

            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    android.util.Log.d(AdConfig.TAG, "Banner loaded")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    android.util.Log.e(
                        AdConfig.TAG,
                        "Banner error: ${error.message}"
                    )
                }
            }
        }
    }

    DisposableEffect(adView) {
        if (AdConfig.sdkReady) {
            adView.loadAd(AdRequest.Builder().build())
        }

        onDispose {
            adView.destroy()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height + 12.dp)
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { adView },
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        )
    }
}
