
package com.shahabmillizai.learnkorean.data.model

import com.google.gson.annotations.SerializedName

data class CurriculumData(
    @SerializedName("curriculum_version")
    val version: String,

    @SerializedName("app_title")
    val appTitle: String,

    @SerializedName("developer")
    val developer: String,

    @SerializedName("levels")
    val levels: List<Level>,

    @SerializedName("dictionary")
    val dictionary: List<DictionaryItem>
)

data class Level(
    val id: String,
    val name: String,

    @SerializedName("korean_name")
    val koreanName: String,

    @SerializedName("urdu_name")
    val urduName: String,

    val description: String,
    val lessons: List<Lesson>
)

data class Lesson(
    val id: String,
    val title: String,

    @SerializedName("korean_title")
    val koreanTitle: String,

    @SerializedName("urdu_title")
    val urduTitle: String,

    val description: String,

    @SerializedName("grammar_notes")
    val grammarNotes: String,

    @SerializedName("urdu_explanation")
    val urduExplanation: String,

    val items: List<LessonItem>,
    val quiz: List<QuizQuestion>
)

data class LessonItem(
    val korean: String,
    val romanization: String,
    val english: String,
    val urdu: String,

    @SerializedName("example_korean")
    val exampleKorean: String,

    @SerializedName("example_english")
    val exampleEnglish: String,

    @SerializedName("example_urdu")
    val exampleUrdu: String
)

data class QuizQuestion(
    val question: String,

    @SerializedName("urdu_question")
    val urduQuestion: String,

    val options: List<String>,

    @SerializedName("correct_index")
    val correctIndex: Int,

    val explanation: String
)

data class DictionaryItem(
    val korean: String,
    val romanization: String,
    val english: String,
    val urdu: String,
    val category: String,
    val level: String,
    var isBookmarked: Boolean = false
)

data class HangulCharacter(
    val char: String,
    val name: String,
    val sound: String,
    val urduSound: String,
    val type: String
)

data class UserProgress(
    val xp: Int = 0,
    val streakDays: Int = 0,
    val completedLessonIds: Set<String> = emptySet(),
    val bookmarkedWords: Set<String> = emptySet(),
    val isBonusPackUnlocked: Boolean = false
)
