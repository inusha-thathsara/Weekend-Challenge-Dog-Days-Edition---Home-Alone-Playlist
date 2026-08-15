package com.pawspeace.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = AccentAmber,
    onPrimary = TextDark,
    primaryContainer = AccentAmberGlow,
    onPrimaryContainer = AccentAmberLight,
    secondary = AccentIndigo,
    onSecondary = TextPrimary,
    secondaryContainer = AccentIndigoGlow,
    onSecondaryContainer = AccentIndigoLight,
    tertiary = AccentEmerald,
    onTertiary = TextDark,
    tertiaryContainer = AccentEmeraldGlow,
    onTertiaryContainer = AccentEmeraldLight,
    background = BgDark,
    onBackground = TextPrimary,
    surface = BgCard,
    onSurface = TextPrimary,
    surfaceVariant = BgCardElevated,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderMedium,
    error = AccentRose,
    onError = TextPrimary
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun PawsPeaceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        shapes = AppShapes,
        content = content
    )
}
