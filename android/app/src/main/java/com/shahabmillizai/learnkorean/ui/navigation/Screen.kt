package com.shahabmillizai.learnkorean.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Lessons : Screen("lessons", "Lessons")
    object LessonDetail : Screen("lesson_detail/{lessonId}", "Lesson Detail") {
        fun createRoute(lessonId: String) = "lesson_detail/$lessonId"
    }
    object Practice : Screen("practice", "Practice")
    object Dictionary : Screen("dictionary", "Dictionary")
    object Profile : Screen("profile", "Profile")
}
