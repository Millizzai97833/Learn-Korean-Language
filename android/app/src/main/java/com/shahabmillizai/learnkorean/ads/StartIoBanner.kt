package com.shahabmillizai.learnkorean.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.startapp.sdk.ads.banner.Banner

@Composable
fun StartIoBanner() {
    if (!StartIoSdkState.isReady) return

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        factory = { context ->
            Banner(context)
        }
    )
}
