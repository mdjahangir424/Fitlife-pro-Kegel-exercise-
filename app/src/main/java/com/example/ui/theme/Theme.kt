package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FitLifeDarkColorScheme = darkColorScheme(
    primary = NeonEmerald,
    onPrimary = Color.Black,
    primaryContainer = EmeraldDark,
    onPrimaryContainer = Color.White,
    secondary = AccentCyan,
    onSecondary = Color.Black,
    secondaryContainer = AccentBlue,
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

private val FitLifeLightColorScheme = darkColorScheme(
    primary = NeonEmerald,
    onPrimary = Color.Black,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder
)

@Composable
fun FitLifeProTheme(
    darkTheme: Boolean = true, // FitLife Pro is styled with a gorgeous fitness dark UI
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FitLifeDarkColorScheme,
        typography = Typography,
        content = content
    )
}
