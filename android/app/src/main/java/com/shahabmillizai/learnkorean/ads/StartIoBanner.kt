package com.shahabmillizai.learnkorean.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Convenient Composable entry point for Start.io banners.
 * By default provides the large, tall MREC banner (300x250) for primary placements.
 */
@Composable
fun StartIoBanner(
    modifier: Modifier = Modifier,
    isLarge: Boolean = true
) {
    BannerAdView(
        modifier = modifier,
        bannerSize = if (isLarge) StartIoBannerSize.LARGE_MREC else StartIoBannerSize.STANDARD
    )
}
