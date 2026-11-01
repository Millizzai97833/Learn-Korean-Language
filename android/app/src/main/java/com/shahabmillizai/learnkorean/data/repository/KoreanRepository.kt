package com.shahabmillizai.learnkorean.data.repository

import android.content.Context
import com.google.gson.Gson
import com.shahabmillizai.learnkorean.data.local.UserPreferences
import com.shahabmillizai.learnkorean.data.model.CurriculumData
import com.shahabmillizai.learnkorean.data.model.DictionaryItem
import com.shahabmillizai.learnkorean.data.model.HangulCharacter
import com.shahabmillizai.learnkorean.data.model.Level
import com.shahabmillizai.learnkorean.data.model.Lesson
import java.io.InputStreamReader

/**
 * Offline Korean Learning Repository:
 * Loads curriculum data directly from local APK assets (no internet required for learning).
 */
class KoreanRepository(
    private val context: Context,
    val userPreferences: UserPreferences
) {
    private val gson = Gson()
    private var cachedData: CurriculumData? = null

    init {
        loadCurriculum()
    }

    private fun loadCurriculum(): CurriculumData {
        cachedData?.let { return it }
        val stream = context.assets.open("curriculum.json")
        val reader = InputStreamReader(stream)
        val data = gson.fromJson(reader, CurriculumData::class.java)
        cachedData = data
        return data
    }

    fun getLevels(): List<Level> {
        return loadCurriculum().levels
    }

    fun getLessonById(lessonId: String): Lesson? {
        for (level in getLevels()) {
            for (lesson in level.lessons) {
                if (lesson.id == lessonId) return lesson
            }
        }
        return null
    }

    fun getDictionary(): List<DictionaryItem> {
        val dict = loadCurriculum().dictionary
        val bookmarked = userPreferences.progressFlow.value.bookmarkedWords
        return dict.map { it.copy(isBookmarked = bookmarked.contains(it.korean)) }
    }

    fun searchDictionary(query: String): List<DictionaryItem> {
        if (query.isBlank()) return getDictionary()
        val q = query.trim().lowercase()
        return getDictionary().filter {
            it.korean.lowercase().contains(q) ||
            it.romanization.lowercase().contains(q) ||
            it.english.lowercase().contains(q) ||
            it.urdu.contains(query.trim())
        }
    }

    fun getHangulAlphabet(): List<HangulCharacter> {
        return listOf(
            HangulCharacter("ㄱ", "Giyeok", "g/k", "گ یا ک", "Consonant"),
            HangulCharacter("ㄴ", "Nieun", "n", "ن", "Consonant"),
            HangulCharacter("ㄷ", "Digeut", "d/t", "د یا ت", "Consonant"),
            HangulCharacter("ㄹ", "Rieul", "r/l", "ر یا ل", "Consonant"),
            HangulCharacter("ㅁ", "Mieum", "m", "م", "Consonant"),
            HangulCharacter("ㅂ", "Bieup", "b/p", "ب یا پ", "Consonant"),
            HangulCharacter("ㅅ", "Siot", "s", "س", "Consonant"),
            HangulCharacter("ㅇ", "Ieung", "- / ng", "خاموش / نگ", "Consonant"),
            HangulCharacter("ㅈ", "Jieut", "j", "ج", "Consonant"),
            HangulCharacter("ㅊ", "Chieut", "ch", "چ", "Consonant"),
            HangulCharacter("ㅋ", "Kieuk", "k", "کھ", "Consonant"),
            HangulCharacter("ㅌ", "Tieut", "t", "تھ", "Consonant"),
            HangulCharacter("ㅍ", "Pieup", "p", "پھ", "Consonant"),
            HangulCharacter("ㅎ", "Hieut", "h", "ہ", "Consonant"),
            HangulCharacter("ㅏ", "a", "ah", "آ", "Vowel"),
            HangulCharacter("ㅑ", "ya", "yah", "یا", "Vowel"),
            HangulCharacter("ㅓ", "eo", "uh", "اَ", "Vowel"),
            HangulCharacter("ㅕ", "yeo", "yuh", "یَ", "Vowel"),
            HangulCharacter("ㅗ", "o", "oh", "او", "Vowel"),
            HangulCharacter("ㅛ", "yo", "yoh", "یو", "Vowel"),
            HangulCharacter("ㅜ", "u", "oo", "اُو", "Vowel"),
            HangulCharacter("ㅠ", "yu", "yoo", "یُو", "Vowel"),
            HangulCharacter("ㅡ", "eu", "eu", "اِو", "Vowel"),
            HangulCharacter("ㅣ", "i", "ee", "ای", "Vowel")
        )
    }
}
