
package com.shahabmillizai.learnkorean

import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.google.android.gms.ads.MobileAds
import com.yandex.mobile.ads.common.YandexAds

import com.shahabmillizai.learnkorean.ads.AdConfig
import com.shahabmillizai.learnkorean.ads.ConsentManager
import com.shahabmillizai.learnkorean.ads.InterstitialAdManager
import com.shahabmillizai.learnkorean.ads.RewardedInterstitialAdManager

import com.shahabmillizai.learnkorean.data.local.UserPreferences
import com.shahabmillizai.learnkorean.data.repository.KoreanRepository
import com.shahabmillizai.learnkorean.tts.KoreanTtsManager
import com.shahabmillizai.learnkorean.ui.navigation.MainAppNavigation
import com.shahabmillizai.learnkorean.ui.theme.LearnKoreanTheme

class MainActivity : ComponentActivity() {

    private lateinit var ttsManager: KoreanTtsManager
    private lateinit var consentManager: ConsentManager
    private lateinit var interstitialManager: InterstitialAdManager
    private lateinit var rewardedManager: RewardedInterstitialAdManager
    private lateinit var repository: KoreanRepository

    private var appReady by mutableStateOf(false)

    private var adsInitializationStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferences = UserPreferences(this)

        repository = KoreanRepository(
            this,
            userPreferences
        )

        ttsManager = KoreanTtsManager(this)

        consentManager = ConsentManager(this)

        interstitialManager = InterstitialAdManager(this)

        rewardedManager = RewardedInterstitialAdManager(this)

        setContent {
            LearnKoreanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (appReady) {
                        MainAppNavigation(
                            repository = repository,
                            ttsManager = ttsManager,
                            interstitialManager = interstitialManager,
                            rewardedManager = rewardedManager,
                            consentManager = consentManager
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator()

                                Text("Preparing Learn Korean...")
                            }
                        }
                    }
                }
            }
        }

        consentManager.gatherConsent(this) {
            if (consentManager.canRequestAds) {
                initializeAds()
            } else {
                appReady = true
            }
        }
    }

    private fun initializeAds() {
        if (
            adsInitializationStarted ||
            isFinishing ||
            isDestroyed
        ) {
            return
        }

        adsInitializationStarted = true

        // Initialize Google AdMob for existing banner/interstitial ads.
        MobileAds.initialize(this) { status ->

            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread

                AdConfig.sdkReady = true

                Log.i(
                    AdConfig.TAG,
                    "AdMob initialized: ${status.adapterStatusMap}"
                )

                interstitialManager.loadAd()

                appReady = true
            }
        }

        // Initialize Yandex only after the consent check.
        try {
            YandexAds.setUserConsent(true)

            YandexAds.initialize(applicationContext) {

                runOnUiThread {
                    if (isFinishing || isDestroyed) {
                        return@runOnUiThread
                    }

                    AdConfig.yandexSdkReady = true

                    Log.i(
                        AdConfig.TAG,
                        "Yandex Mobile Ads initialized successfully"
                    )

                    rewardedManager.loadAd()
                }
            }

        } catch (exception: Exception) {
            Log.e(
                AdConfig.TAG,
                "Yandex initialization failed",
                exception
            )
        }
    }

    override fun onResume() {
        super.onResume()

        if (::interstitialManager.isInitialized &&
            AdConfig.sdkReady
        ) {
            interstitialManager.loadAd()
        }

        if (::rewardedManager.isInitialized &&
            AdConfig.yandexSdkReady
        ) {
            rewardedManager.loadAd()
        }
    }

    override fun onDestroy() {
        if (::ttsManager.isInitialized) {
            ttsManager.shutdown()
        }

        if (::rewardedManager.isInitialized) {
            rewardedManager.destroy()
        }

        super.onDestroy()
    }
}
