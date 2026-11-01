package com.shahabmillizai.learnkorean.ads

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.shahabmillizai.learnkorean.R

/**
 * Official Google Native Ad Component:
 * Harmoniously blends with Korean learning cards while strictly adhering
 * to Google AdMob Native Ad Policies (AD attribution badge, clear clickable elements).
 */
@Composable
fun KoreanNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String = AdConfig.getNativeAdUnitId()
) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var isLoaded by remember { mutableStateOf(false) }

    DisposableEffect(adUnitId) {
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                nativeAd?.destroy()
                nativeAd = ad
                isLoaded = true
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isLoaded = false
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())

        onDispose {
            nativeAd?.destroy()
        }
    }

    if (isLoaded && nativeAd != null) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            AndroidView(
                factory = { ctx ->
                    val adView = NativeAdView(ctx)
                    val container = android.widget.LinearLayout(ctx).apply {
                        orientation = android.widget.LinearLayout.VERTICAL
                    }

                    // AD attribution badge
                    val badge = TextView(ctx).apply {
                        text = "SPONSORED AD"
                        textSize = 10f
                        setTextColor(android.graphics.Color.WHITE)
                        setBackgroundColor(android.graphics.Color.parseColor("#CD2E3A"))
                        setPadding(12, 4, 12, 4)
                    }
                    container.addView(badge)

                    // Headline
                    val headlineView = TextView(ctx).apply {
                        textSize = 15f
                        setTypeface(null, android.graphics.Typeface.BOLD)
                        setTextColor(android.graphics.Color.parseColor("#0F172A"))
                        setPadding(0, 8, 0, 4)
                    }
                    container.addView(headlineView)
                    adView.headlineView = headlineView

                    // Body
                    val bodyView = TextView(ctx).apply {
                        textSize = 13f
                        setTextColor(android.graphics.Color.parseColor("#475569"))
                        setPadding(0, 0, 0, 8)
                    }
                    container.addView(bodyView)
                    adView.bodyView = bodyView

                    // Call to Action Button
                    val ctaButton = Button(ctx).apply {
                        setBackgroundColor(android.graphics.Color.parseColor("#0047A0"))
                        setTextColor(android.graphics.Color.WHITE)
                    }
                    container.addView(ctaButton)
                    adView.callToActionView = ctaButton

                    adView.addView(container)
                    adView
                },
                update = { view ->
                    val ad = nativeAd ?: return@AndroidView
                    (view.headlineView as? TextView)?.text = ad.headline
                    (view.bodyView as? TextView)?.text = ad.body
                    (view.callToActionView as? Button)?.text = ad.callToAction ?: "Learn More"
                    view.setNativeAd(ad)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
