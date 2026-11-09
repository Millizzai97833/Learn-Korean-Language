
package com.shahabmillizai.learnkorean.data.local

import android.content.Context
import android.content.SharedPreferences
import com.shahabmillizai.learnkorean.data.model.UserProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            "learn_korean_prefs",
            Context.MODE_PRIVATE
        )

    private val _progressFlow =
        MutableStateFlow(loadProgress())

    val progressFlow: StateFlow<UserProgress> =
        _progressFlow.asStateFlow()

    private fun loadProgress(): UserProgress {
        val xp = prefs.getInt("key_xp", 0)
        val streak = prefs.getInt("key_streak", 0)

        val completed = (
            prefs.getStringSet(
                "key_completed_lessons",
                emptySet()
            ) ?: emptySet()
        ).toSet()

        val bookmarks = (
            prefs.getStringSet(
                "key_bookmarks",
                emptySet()
            ) ?: emptySet()
        ).toSet()

        val bonus = prefs.getBoolean(
            "key_bonus_pack",
            false
        )

        return UserProgress(
            xp = xp,
            streakDays = streak,
            completedLessonIds = completed,
            bookmarkedWords = bookmarks,
            isBonusPackUnlocked = bonus
        )
    }

    fun addXp(amount: Int) {
        if (amount <= 0) return

        val current = _progressFlow.value
        val updated = current.copy(
            xp = current.xp + amount
        )

        prefs.edit()
            .putInt("key_xp", updated.xp)
            .apply()

        _progressFlow.value = updated
    }

    fun markLessonCompleted(lessonId: String) {
        if (lessonId.isBlank()) return

        val current = _progressFlow.value

        // Do not award XP repeatedly for the same lesson.
        if (current.completedLessonIds.contains(lessonId)) {
            return
        }

        val updatedSet =
            current.completedLessonIds + lessonId

        val updated = current.copy(
            completedLessonIds = updatedSet,
            xp = current.xp + 50
        )

        prefs.edit()
            .putStringSet(
                "key_completed_lessons",
                updatedSet
            )
            .putInt("key_xp", updated.xp)
            .apply()

        _progressFlow.value = updated
    }

    fun toggleBookmark(word: String) {
        if (word.isBlank()) return

        val current = _progressFlow.value

        val updatedSet =
            if (current.bookmarkedWords.contains(word)) {
                current.bookmarkedWords - word
            } else {
                current.bookmarkedWords + word
            }

        prefs.edit()
            .putStringSet("key_bookmarks", updatedSet)
            .apply()

        _progressFlow.value = current.copy(
            bookmarkedWords = updatedSet
        )
    }

    fun unlockBonusPack() {
        val current = _progressFlow.value

        // Prevent multiple reward payments for one unlock.
        if (current.isBonusPackUnlocked) return

        val updated = current.copy(
            isBonusPackUnlocked = true,
            xp = current.xp + 150
        )

        prefs.edit()
            .putBoolean("key_bonus_pack", true)
            .putInt("key_xp", updated.xp)
            .apply()

        _progressFlow.value = updated
    }
}
