package com.shahabmillizai.learnkorean.data.local

import android.content.Context
import android.content.SharedPreferences
import com.shahabmillizai.learnkorean.data.model.UserProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Handles offline persistence for user learning streak, XP points,
 * completed lessons, and saved bookmarks using SharedPreferences.
 */
class UserPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("learn_korean_prefs", Context.MODE_PRIVATE)

    private val _progressFlow = MutableStateFlow(loadProgress())
    val progressFlow: StateFlow<UserProgress> = _progressFlow.asStateFlow()

    private fun loadProgress(): UserProgress {
        val xp = prefs.getInt("key_xp", 150)
        val streak = prefs.getInt("key_streak", 5)
        val completed = prefs.getStringSet("key_completed_lessons", setOf("beg_01")) ?: setOf("beg_01")
        val bookmarks = prefs.getStringSet("key_bookmarks", setOf("안녕하세요", "감사합니다")) ?: setOf()
        val bonus = prefs.getBoolean("key_bonus_pack", false)

        return UserProgress(
            xp = xp,
            streakDays = streak,
            completedLessonIds = completed,
            bookmarkedWords = bookmarks,
            isBonusPackUnlocked = bonus
        )
    }

    fun addXp(amount: Int) {
        val current = _progressFlow.value
        val updated = current.copy(xp = current.xp + amount)
        prefs.edit().putInt("key_xp", updated.xp).apply()
        _progressFlow.value = updated
    }

    fun markLessonCompleted(lessonId: String) {
        val current = _progressFlow.value
        val updatedSet = current.completedLessonIds + lessonId
        val updated = current.copy(
            completedLessonIds = updatedSet,
            xp = current.xp + 50
        )
        prefs.edit()
            .putStringSet("key_completed_lessons", updatedSet)
            .putInt("key_xp", updated.xp)
            .apply()
        _progressFlow.value = updated
    }

    fun toggleBookmark(word: String) {
        val current = _progressFlow.value
        val updatedSet = if (current.bookmarkedWords.contains(word)) {
            current.bookmarkedWords - word
        } else {
            current.bookmarkedWords + word
        }
        prefs.edit().putStringSet("key_bookmarks", updatedSet).apply()
        _progressFlow.value = current.copy(bookmarkedWords = updatedSet)
    }

    fun unlockBonusPack() {
        prefs.edit().putBoolean("key_bonus_pack", true).apply()
        val current = _progressFlow.value
        _progressFlow.value = current.copy(isBonusPackUnlocked = true, xp = current.xp + 150)
    }
}
