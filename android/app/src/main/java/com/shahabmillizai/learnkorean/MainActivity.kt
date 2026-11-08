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

import com.shahabmillizai.learnkorean.ads.AdConfig
import com.shahabmillizai.learnkorean.ads.ConsentManager
import com.shahabmillizai.learnkorean.ads.InterstitialAdManager
import com.shahabmillizai.learnkorean.ads.RewardedInterstitialAdManager
import com.shahabmillizai.learnkorean.ads.StartIoSdkState
import com.shahabmillizai.learnkorean.data.local.UserPreferences
import com.shahabmillizai.learnkorean.data.repository.KoreanRepository
import com.shahabmillizai.learnkorean.tts.KoreanTtsManager
import com.shahabmillizai.learnkorean.ui.navigation.MainAppNavigation
import com.shahabmillizai.learnkorean.ui.theme.LearnKoreanTheme
import com.startapp.sdk.adsbase.StartAppSDK

class MainActivity : ComponentActivity() {

    private lateinit var ttsManager: KoreanTtsManager
    private lateinit var consentManager: ConsentManager
    private lateinit var interstitialManager: InterstitialAdManager
    private lateinit var rewardedManager: RewardedInterstitialAdManager
    private lateinit var repository: KoreanRepository

    private var appReady by mutableStateOf(false)
    private var sdkInitializationStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferences = UserPreferences(this)
        repository = KoreanRepository(this, userPreferences)
        ttsManager = KoreanTtsManager(this)
        consentManager = ConsentManager(this)

        // Instantiate ad managers (they internally wait for StartIoSdkState.isReady before loading)
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

        // Gather consent first, then initiate Start.io SDK
        consentManager.gatherConsent(this) {
            initializeStartIo()
        }

        // App content is ready to render
        appReady = true
    }

    private fun initializeStartIo() {
        if (sdkInitializationStarted) return
        if (isFinishing || isDestroyed) return

        sdkInitializationStarted = true

        try {
            if (AdConfig.forceTestMode) {
                StartAppSDK.setTestAdsEnabled(true)
                Log.d(AdConfig.TAG_SDK, "Start.io test ads enabled for debug environment")
            }

            Log.i(
                AdConfig.TAG_SDK,
                "Initializing Start.io SDK (App ID: ${AdConfig.START_IO_APP_ID})..."
            )

            StartAppSDK.initParams(
                applicationContext,
                AdConfig.START_IO_APP_ID
            )
                .setReturnAdsEnabled(false)
                .setCallback(Runnable {
                    runOnUiThread {
                        Log.i(
                            AdConfig.TAG_SDK,
                            "Start.io SDK initialization callback confirmed success"
                        )
                        StartIoSdkState.markReady()
                    }
                })
                .init()

        } catch (e: Exception) {
            Log.e(
                AdConfig.TAG_SDK,
                "Start.io SDK initialization failed with exception: ${e.message}",
                e
            )
            // CRITICAL: Do NOT mark SDK as ready on failure
            StartIoSdkState.markFailed(e.message ?: "Initialization exception")
        }
    }

    override fun onResume() {
        super.onResume()

        if (StartIoSdkState.isSdkReady()) {
            if (::interstitialManager.isInitialized) {
                interstitialManager.loadAd()
            }
            if (::rewardedManager.isInitialized) {
                rewardedManager.loadAd()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::ttsManager.isInitialized) {
            ttsManager.shutdown()
        }
    }
}
