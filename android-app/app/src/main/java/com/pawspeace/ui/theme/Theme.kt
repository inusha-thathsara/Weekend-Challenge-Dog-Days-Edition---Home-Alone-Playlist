package com.pawspeace.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AccentAmber,
    secondary = AccentIndigo,
    tertiary = AccentEmerald,
    background = BgDark,
    surface = BgCard,
    onPrimary = BgDark,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = AccentRose
)

@Composable
fun PawsPeaceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
