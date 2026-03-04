package com.saptarshi.aipal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dark theme — the primary look of the app. Deep navy backgrounds, electric blue accents.
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = TextWhite,
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = PrimaryBlueLight,

    secondary = AccentCyan,
    onSecondary = DarkNavy,
    secondaryContainer = SurfaceNavy,
    onSecondaryContainer = AccentCyan,

    background = DarkNavy,
    onBackground = TextWhite,

    surface = DeepNavy,
    onSurface = TextWhite,
    surfaceVariant = SurfaceNavy,
    onSurfaceVariant = TextGray,

    error = ErrorRed,
    onError = TextWhite
)

// Light theme — clean and bright, but still uses the blue identity.
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = LightSurface,
    primaryContainer = PrimaryBlueLight,
    onPrimaryContainer = PrimaryBlueDark,

    secondary = AccentCyan,
    onSecondary = TextDark,
    secondaryContainer = Color(0xFFE0F7FA),
    onSecondaryContainer = Color(0xFF00695C),

    background = LightBackground,
    onBackground = TextDark,

    surface = LightSurface,
    onSurface = TextDark,
    surfaceVariant = Color(0xFFE8ECF1),
    onSurfaceVariant = TextMuted,

    error = ErrorRed,
    onError = LightSurface
)

@Composable
fun AiPalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AiPalTypography,
        shapes = AiPalShapes,
        content = content
    )
}