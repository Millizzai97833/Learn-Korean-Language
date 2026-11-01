package com.shahabmillizai.learnkorean.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = TaegeukBlueLight,
    onPrimary = KwaeBlack,
    primaryContainer = TaegeukBlueDark,
    onPrimaryContainer = PureWhite,
    secondary = TaegeukRedLight,
    onSecondary = KwaeBlack,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurface,
    outline = BorderDark,
    outlineVariant = BorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = TaegeukBlue,
    onPrimary = PureWhite,
    primaryContainer = Color(0xFFE3EDFA),
    onPrimaryContainer = TaegeukBlueDark,
    secondary = TaegeukRed,
    onSecondary = PureWhite,
    background = CanvasLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceCard,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF1F5F9),
    outline = BorderLight,
    outlineVariant = BorderLight
)

@Composable
fun LearnKoreanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
