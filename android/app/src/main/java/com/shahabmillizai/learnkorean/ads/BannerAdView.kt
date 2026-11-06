
package com.shahabmillizai.learnkorean.ads

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.startapp.sdk.ads.banner.Banner

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier
) {
    if (StartIoSdkState.isReady) {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    Banner(context)
                }
            )
        }
    }
}
