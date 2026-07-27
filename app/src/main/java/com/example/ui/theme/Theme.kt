package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = IndigoLight,
    onPrimary = NavyDarkBackground,
    primaryContainer = NavyDarkCard,
    onPrimaryContainer = TextPrimaryDark,
    secondary = CyanAccent,
    onSecondary = NavyDarkBackground,
    secondaryContainer = NavyDarkCard,
    onSecondaryContainer = CyanAccentLight,
    tertiary = MintSuccess,
    background = NavyDarkBackground,
    onBackground = TextPrimaryDark,
    surface = NavyDarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = NavyDarkCard,
    onSurfaceVariant = TextSecondaryDark,
    outline = NavyDarkBorder,
    error = RoseDanger
)

private val LightColorScheme = lightColorScheme(
    primary = IndigoPrimary,
    onPrimary = SlateLightSurface,
    primaryContainer = SlateLightCard,
    onPrimaryContainer = IndigoPrimary,
    secondary = CyanAccent,
    onSecondary = SlateLightSurface,
    secondaryContainer = SlateLightCard,
    onSecondaryContainer = TextPrimaryLight,
    tertiary = EmeraldGreen,
    background = SlateLightBackground,
    onBackground = TextPrimaryLight,
    surface = SlateLightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = SlateLightCard,
    onSurfaceVariant = TextSecondaryLight,
    outline = SlateLightBorder,
    error = RoseDanger
)

@Composable
fun StudySquadTheme(
    darkTheme: Boolean = true, // Default to clean dark focus mode for academic concentration
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
