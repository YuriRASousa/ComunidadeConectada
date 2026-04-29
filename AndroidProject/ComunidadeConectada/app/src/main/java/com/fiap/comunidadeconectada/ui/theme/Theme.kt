package com.fiap.comunidadeconectada.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryGreen,
    tertiary = HighlightAmber,
    background = BackgroundDark,
    surface = Color(0xFF1F1F1F),
    onBackground = TextDark,
    onSurface = TextDark,
    error = ErrorRed,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryGreen,
    tertiary = HighlightAmber,
    background = BackgroundLight,
    surface = Color(0xFFFAFAFA),
    onBackground = TextLight,
    onSurface = TextLight,
    error = ErrorRed,
)

@Composable
fun ComunidadeConectadaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
