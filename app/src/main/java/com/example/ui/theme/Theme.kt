package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

enum class AppThemeMode {
    DARK,
    LIGHT,
    SYSTEM
}

private val FitLifeDarkColorScheme = darkColorScheme(
    primary = SkyBlueLight,
    onPrimary = Color.Black,
    primaryContainer = SkyBlueDark,
    onPrimaryContainer = Color.White,
    secondary = AccentCyan,
    onSecondary = Color.Black,
    secondaryContainer = SkyBlueContainer,
    onSecondaryContainer = Color.White,
    tertiary = AccentOrange,
    onTertiary = Color.Black,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder
)

private val FitLifeLightColorScheme = lightColorScheme(
    primary = SkyBlueDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBAE6FD),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = Color(0xFF0284C7),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0C4A6E),
    tertiary = AccentOrange,
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1)
)

val LocalThemeMode = compositionLocalOf { AppThemeMode.DARK }

@Composable
fun isDarkTheme(): Boolean {
    val mode = LocalThemeMode.current
    val systemDark = isSystemInDarkTheme()
    return when (mode) {
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
        AppThemeMode.SYSTEM -> systemDark
    }
}

@Composable
fun appBackgroundColor(): Color = if (isDarkTheme()) BackgroundDark else Color(0xFFF8FAFC)

@Composable
fun appSurfaceColor(): Color = if (isDarkTheme()) SurfaceCard else Color(0xFFFFFFFF)

@Composable
fun appCardItemColor(): Color = if (isDarkTheme()) Color(0xFF131D31) else Color(0xFFF1F5F9)

@Composable
fun appBorderColor(): Color = if (isDarkTheme()) SurfaceBorder else Color(0xFFE2E8F0)

@Composable
fun appTextPrimary(): Color = if (isDarkTheme()) TextPrimary else Color(0xFF0F172A)

@Composable
fun appTextSecondary(): Color = if (isDarkTheme()) TextSecondary else Color(0xFF475569)

@Composable
fun appTextMuted(): Color = if (isDarkTheme()) TextMuted else Color(0xFF64748B)

@Composable
fun FitLifeProTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
        AppThemeMode.SYSTEM -> isSystemDark
    }

    val colorScheme = if (isDark) FitLifeDarkColorScheme else FitLifeLightColorScheme

    CompositionLocalProvider(LocalThemeMode provides themeMode) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
