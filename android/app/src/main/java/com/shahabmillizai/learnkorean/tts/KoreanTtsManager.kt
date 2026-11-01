package com.shahabmillizai.learnkorean.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

/**
 * TextToSpeech engine configured specifically for Korean Hangul pronunciation.
 */
class KoreanTtsManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.KOREA)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("KoreanTtsManager", "Korean language is not supported or missing voice data")
                // Fallback to standard Korean locale
                tts?.setLanguage(Locale.KOREAN)
            }
            tts?.setSpeechRate(0.85f) // Slightly slower for clear language learners
            isInitialized = true
        } else {
            Log.e("KoreanTtsManager", "Failed to initialize TextToSpeech engine")
        }
    }

    fun speakKorean(text: String) {
        if (isInitialized && tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "korean_tts_${System.currentTimeMillis()}")
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
