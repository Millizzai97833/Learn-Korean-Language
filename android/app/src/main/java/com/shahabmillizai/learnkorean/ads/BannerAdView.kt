
package com.shahabmillizai.learnkorean.ads

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.startapp.sdk.ads.banner.BannerFormat
import com.startapp.sdk.ads.banner.BannerListener
import com.startapp.sdk.ads.banner.BannerRequest

enum class StartIoBannerSize {
    STANDARD,
    LARGE_MREC
}

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    bannerSize: StartIoBannerSize = StartIoBannerSize.STANDARD
) {
    val context = LocalContext.current.applicationContext

    var adView by remember(bannerSize) {
        mutableStateOf<View?>(null)
    }

    val isSdkReady = StartIoSdkState.isReady

    DisposableEffect(context, bannerSize, isSdkReady) {
        var disposed = false

        if (isSdkReady) {
            val format = when (bannerSize) {
                StartIoBannerSize.STANDARD -> BannerFormat.BANNER
                StartIoBannerSize.LARGE_MREC -> BannerFormat.MREC
            }

            Log.i(
                AdConfig.TAG_BANNER,
                "Requesting Start.io banner: $format"
            )

            try {
                BannerRequest(context)
                    .setAdFormat(format)
                    .load { creator, error ->

                        Handler(Looper.getMainLooper()).post {

                            if (disposed) return@post

                            if (creator == null) {
                                Log.e(
                                    AdConfig.TAG_BANNER,
                                    "Banner request failed: ${error ?: "Unknown error"}"
                                )
                                return@post
                            }

                            try {
                                val createdView = creator.create(
                                    context,
                                    object : BannerListener {

                                        override fun onReceiveAd(
                                            banner: View
                                        ) {
                                            Log.i(
                                                AdConfig.TAG_BANNER,
                                                "Banner received successfully"
                                            )
                                        }

                                        override fun onFailedToReceiveAd(
                                            banner: View
                                        ) {
                                            Log.e(
                                                AdConfig.TAG_BANNER,
                                                "Banner failed to receive an ad"
                                            )
                                        }

                                        override fun onImpression(
                                            banner: View
                                        ) {
                                            Log.d(
                                                AdConfig.TAG_BANNER,
                                                "Banner impression recorded"
                                            )
                                        }

                                        override fun onClick(
                                            banner: View
                                        ) {
                                            Log.d(
                                                AdConfig.TAG_BANNER,
                                                "Banner clicked"
                                            )
                                        }
                                    }
                                )

                                adView = createdView

                                Log.i(
                                    AdConfig.TAG_BANNER,
                                    "Banner view created successfully"
                                )

                            } catch (e: Exception) {
                                Log.e(
                                    AdConfig.TAG_BANNER,
                                    "Could not create banner view",
                                    e
                                )
                            }
                        }
                    }

            } catch (e: Exception) {
                Log.e(
                    AdConfig.TAG_BANNER,
                    "Banner request exception",
                    e
                )
            }
        } else {
            Log.d(
                AdConfig.TAG_BANNER,
                "Waiting for Start.io SDK initialization"
            )
        }

        onDispose {
            disposed = true

            adView?.let { view ->
                (view.parent as? ViewGroup)?.removeView(view)
            }

            adView = null

            Log.d(
                AdConfig.TAG_BANNER,
                "Banner view disposed"
            )
        }
    }

    val currentAdView = adView

    val widthDp = when (bannerSize) {
        StartIoBannerSize.STANDARD -> 320
        StartIoBannerSize.LARGE_MREC -> 300
    }

    val heightDp = when (bannerSize) {
        StartIoBannerSize.STANDARD -> 50
        StartIoBannerSize.LARGE_MREC -> 250
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        if (currentAdView != null) {
            AndroidView(
                factory = { currentAdView },
                modifier = Modifier
                    .width(widthDp.dp)
                    .height(heightDp.dp),
                update = {
                    // Do not reload ads during Compose recomposition.
                }
            )
        }
    }
}
