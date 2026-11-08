package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.startapp.sdk.ads.banner.Banner
import com.startapp.sdk.ads.banner.BannerListener
import com.startapp.sdk.ads.banner.Mrec

/**
 * Supported banner sizes for Start.io advertising.
 * LARGE_MREC (300x250) provides the big, tall banner requested for the Home screen.
 * STANDARD (320x50) is used for compact inline or bottom placement on other screens.
 */
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

/**
 * Production-ready Jetpack Compose wrapper for Start.io Banner and MREC ads.
 *
 * Features:
 * - Gated strictly on StartIoSdkState.isReady (no premature ad requests)
 * - Supported dimensions without distortion:
 *     - LARGE_MREC: 300dp x 250dp centered horizontally
 *     - STANDARD: 320dp x 50dp
 * - BannerListener logging load, failure, impression, and click events
 * - Lifecycle-aware: hides on PAUSE/DESTROY, shows on RESUME
 * - Recomposition-protected: ad is never re-requested during Compose state changes
 * - Clean disposal upon leaving composition
 */
@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    bannerSize: StartIoBannerSize = StartIoBannerSize.STANDARD
) {
    // Only attempt to load banner if SDK has confirmed successful initialization
    if (!StartIoSdkState.isReady) {
        return
    }

    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val lifecycleOwner = LocalLifecycleOwner.current

    var bannerViewRef by remember { mutableStateOf<View?>(null) }
    var isAdReceived by remember { mutableStateOf(false) }

    // Manage activity/screen lifecycle for the banner view
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    bannerViewRef?.let { v ->
                        when (v) {
                            is Mrec -> v.showBanner()
                            is Banner -> v.showBanner()
                        }
                        Log.d(AdConfig.TAG_BANNER, "Lifecycle ON_RESUME: showing $bannerSize banner")
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    bannerViewRef?.let { v ->
                        when (v) {
                            is Mrec -> v.hideBanner()
                            is Banner -> v.hideBanner()
                        }
                        Log.d(AdConfig.TAG_BANNER, "Lifecycle ON_PAUSE: hiding $bannerSize banner")
                    }
                }
                Lifecycle.Event.ON_DESTROY -> {
                    bannerViewRef?.let { v ->
                        when (v) {
                            is Mrec -> v.hideBanner()
                            is Banner -> v.hideBanner()
                        }
                        Log.d(AdConfig.TAG_BANNER, "Lifecycle ON_DESTROY: cleaning up $bannerSize banner")
                    }
                    bannerViewRef = null
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            bannerViewRef?.let { v ->
                when (v) {
                    is Mrec -> v.hideBanner()
                    is Banner -> v.hideBanner()
                }
            }
            bannerViewRef = null
            Log.d(AdConfig.TAG_BANNER, "BannerAdView ($bannerSize) disposed from composition")
        }
    }

    when (bannerSize) {
        StartIoBannerSize.LARGE_MREC -> {
            // Big tall banner: 300dp x 250dp, centered horizontally, no clipping or distortion
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    modifier = Modifier.size(
                        width = AdConfig.MREC_WIDTH_DP.dp,
                        height = AdConfig.MREC_HEIGHT_DP.dp
                    ),
                    factory = { viewContext ->
                        Log.i(
                            AdConfig.TAG_BANNER,
                            "Creating Start.io Large MREC banner (${AdConfig.MREC_WIDTH_DP}x${AdConfig.MREC_HEIGHT_DP} dp)"
                        )

                        val adContext = activity ?: viewContext

                        val mrec = Mrec(adContext, object : BannerListener {
                            override fun onReceiveAd(view: View) {
                                isAdReceived = true
                                (view as? Mrec)?.showBanner()
                                Log.i(
                                    AdConfig.TAG_BANNER,
                                    "Start.io Large MREC banner loaded successfully (display: ${view.width}x${view.height})"
                                )
                            }

                            override fun onFailedToReceiveAd(view: View) {
                                isAdReceived = false
                                (view as? Mrec)?.hideBanner()
                                Log.w(
                                    AdConfig.TAG_BANNER,
                                    "Start.io Large MREC banner failed to load. Start.io may return no-fill depending on network/inventory."
                                )
                            }

                            override fun onImpression(view: View) {
                                Log.d(AdConfig.TAG_BANNER, "Start.io Large MREC impression logged")
                            }

                            override fun onClick(view: View) {
                                Log.d(AdConfig.TAG_BANNER, "Start.io Large MREC banner clicked")
                            }
                        })

                        mrec.layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            android.view.Gravity.CENTER
                        )

                        bannerViewRef = mrec
                        mrec
                    },
                    update = {
                        // Avoid triggering ad reload during Compose recompositions
                    },
                    onRelease = { view ->
                        Log.d(AdConfig.TAG_BANNER, "Releasing Start.io Large MREC view")
                        view.hideBanner()
                    }
                )
            }
        }

        StartIoBannerSize.STANDARD -> {
            // Standard banner: 320x50, centered horizontally
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AdConfig.STANDARD_BANNER_HEIGHT_DP.dp),
                    factory = { viewContext ->
                        Log.i(
                            AdConfig.TAG_BANNER,
                            "Creating Start.io Standard banner (height: ${AdConfig.STANDARD_BANNER_HEIGHT_DP} dp)"
                        )

                        val adContext = activity ?: viewContext

                        val banner = Banner(adContext, object : BannerListener {
                            override fun onReceiveAd(view: View) {
                                isAdReceived = true
                                (view as? Banner)?.showBanner()
                                Log.i(
                                    AdConfig.TAG_BANNER,
                                    "Start.io Standard banner loaded successfully"
                                )
                            }

                            override fun onFailedToReceiveAd(view: View) {
                                isAdReceived = false
                                (view as? Banner)?.hideBanner()
                                Log.w(
                                    AdConfig.TAG_BANNER,
                                    "Start.io Standard banner failed to load"
                                )
                            }

                            override fun onImpression(view: View) {
                                Log.d(AdConfig.TAG_BANNER, "Start.io Standard banner impression logged")
                            }

                            override fun onClick(view: View) {
                                Log.d(AdConfig.TAG_BANNER, "Start.io Standard banner clicked")
                            }
                        })

                        bannerViewRef = banner
                        banner
                    },
                    update = {
                        // Avoid re-requesting ads on recomposition
                    },
                    onRelease = { view ->
                        Log.d(AdConfig.TAG_BANNER, "Releasing Start.io Standard banner view")
                        view.hideBanner()
                    }
                )
            }
        }
    }
}
