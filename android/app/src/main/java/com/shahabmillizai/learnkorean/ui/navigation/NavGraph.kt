package com.shahabmillizai.learnkorean.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.shahabmillizai.learnkorean.ads.ConsentManager
import com.shahabmillizai.learnkorean.ads.InterstitialAdManager
import com.shahabmillizai.learnkorean.ads.RewardedInterstitialAdManager
import com.shahabmillizai.learnkorean.data.repository.KoreanRepository
import com.shahabmillizai.learnkorean.tts.KoreanTtsManager
import com.shahabmillizai.learnkorean.ui.screens.*
import com.shahabmillizai.learnkorean.ui.theme.TaegeukBlue

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

@Composable
fun MainAppNavigation(
    repository: KoreanRepository,
    ttsManager: KoreanTtsManager,
    interstitialManager: InterstitialAdManager,
    rewardedManager: RewardedInterstitialAdManager,
    consentManager: ConsentManager
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home, Icons.Default.Home, "Home"),
        BottomNavItem(Screen.Lessons, Icons.Default.MenuBook, "Lessons"),
        BottomNavItem(Screen.Practice, Icons.Default.FitnessCenter, "Practice"),
        BottomNavItem(Screen.Dictionary, Icons.Default.Search, "Dictionary"),
        BottomNavItem(Screen.Profile, Icons.Default.Person, "Profile")
    )

    val showBottomBar = bottomNavItems.any { it.screen.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.screen.route
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                if (currentRoute != item.screen.route) {
                                    navController.navigate(item.screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = TaegeukBlue.copy(alpha = 0.15f),
                                selectedIconColor = TaegeukBlue,
                                selectedTextColor = TaegeukBlue
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    repository = repository,
                    ttsManager = ttsManager,
                    rewardedManager = rewardedManager,
                    onNavigateToLesson = { lessonId ->
                        navController.navigate(Screen.LessonDetail.createRoute(lessonId))
                    },
                    onNavigateToPractice = {
                        navController.navigate(Screen.Practice.route)
                    }
                )
            }
            composable(Screen.Lessons.route) {
                LessonsScreen(
                    repository = repository,
                    onNavigateToLesson = { lessonId ->
                        navController.navigate(Screen.LessonDetail.createRoute(lessonId))
                    }
                )
            }
            composable(
                route = Screen.LessonDetail.route,
                arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
            ) { backStackEntry ->
                val lessonId = backStackEntry.arguments?.getString("lessonId") ?: "beg_01"
                LessonDetailScreen(
                    lessonId = lessonId,
                    repository = repository,
                    ttsManager = ttsManager,
                    interstitialManager = interstitialManager,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Practice.route) {
                PracticeScreen(
                    repository = repository,
                    ttsManager = ttsManager
                )
            }
            composable(Screen.Dictionary.route) {
                DictionaryScreen(
                    repository = repository,
                    ttsManager = ttsManager
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    repository = repository,
                    consentManager = consentManager
                )
            }
        }
    }
}
