package com.shahabmillizai.learnkorean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize core offline modules & managers
        val userPreferences = UserPreferences(this)
        repository = KoreanRepository(this, userPreferences)
        ttsManager = KoreanTtsManager(this)
        interstitialManager = InterstitialAdManager(this)
        rewardedManager = RewardedInterstitialAdManager(this)
        consentManager = ConsentManager(this)

        // Request Google UMP consent for GDPR compliance where applicable
        consentManager.gatherConsent(this) {
            // Once consent is processed, preload ads safely
            interstitialManager.loadAd()
            rewardedManager.loadAd()
        }

        setContent {
            LearnKoreanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppNavigation(
                        repository = repository,
                        ttsManager = ttsManager,
                        interstitialManager = interstitialManager,
                        rewardedManager = rewardedManager,
                        consentManager = consentManager
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}
