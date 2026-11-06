package com.shahabmillizai.learnkorean.ads

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.startapp.sdk.ads.banner.BannerFormat
import com.startapp.sdk.ads.banner.BannerRequest

private const val TAG = "StartIoBanner"

@Composable
fun BannerAdView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var bannerView by remember { mutableStateOf<View?>(null) }
    var requestStarted by remember { mutableStateOf(false) }

    LaunchedEffect(StartIoSdkState.isReady) {
        if (StartIoSdkState.isReady && !requestStarted) {
            requestStarted = true
            Log.d(TAG, "SDK ready; requesting banner")

            BannerRequest(context.applicationContext)
                .setAdFormat(BannerFormat.BANNER)
                .load { creator, error ->
                    if (creator != null) {
                        val view = creator.create(context.applicationContext, null)
                        Handler(Looper.getMainLooper()).post {
                            bannerView = view
                            Log.d(TAG, "SUCCESS: Banner created")
                        }
                    } else {
                        Log.e(
                            TAG,
                            "FAILED: ${error ?: "No error details"}"
                        )
                    }
                }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        bannerView?.let { view ->
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { view }
            )
        }
    }
}