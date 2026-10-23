package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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

@Composable
fun FitLifeProTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FitLifeDarkColorScheme,
        typography = Typography,
        content = content
    )
}
