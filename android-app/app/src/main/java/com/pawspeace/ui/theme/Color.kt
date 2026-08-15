package com.pawspeace.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Background and Surface hierarchy
val BgDark = Color(0xFF0B0F19)
val BgDeepMidnight = Color(0xFF070A12)
val BgCard = Color(0xFF131B2E)
val BgCardElevated = Color(0xFF19233C)
val BgCardHover = Color(0xFF1F2B48)
val BgGlass = Color(0xD9131B2E)

// Glass Borders & Outlines
val BorderSubtle = Color(0x1FFFFFFF)
val BorderMedium = Color(0x33FFFFFF)
val BorderAccent = Color(0x666366F1)
val BorderAmber = Color(0x66F59E0B)

// Radiant Brand Accents
val AccentAmber = Color(0xFFF59E0B)
val AccentAmberLight = Color(0xFFFBBF24)
val AccentAmberGlow = Color(0x4DF59E0B)

val AccentIndigo = Color(0xFF6366F1)
val AccentIndigoLight = Color(0xFF818CF8)
val AccentIndigoGlow = Color(0x4D6366F1)

val AccentEmerald = Color(0xFF10B981)
val AccentEmeraldLight = Color(0xFF34D399)
val AccentEmeraldGlow = Color(0x4010B981)

val AccentRose = Color(0xFFF43F5E)
val AccentRoseLight = Color(0xFFFB7185)
val AccentRoseGlow = Color(0x40F43F5E)

val AccentCyan = Color(0xFF06B6D4)
val AccentCyanLight = Color(0xFF38BDF8)
val AccentCyanGlow = Color(0x4006B6D4)

val AccentViolet = Color(0xFF8B5CF6)
val AccentVioletLight = Color(0xFFA78BFA)

// Typography & Content Colors
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFFCBD5E1)
val TextMuted = Color(0xFF64748B)
val TextDark = Color(0xFF0B0F19)

// Beautiful Gradients
val AmberGlowGradient = Brush.linearGradient(
    listOf(AccentAmberLight, AccentAmber)
)

val HeroPlayGradient = Brush.linearGradient(
    listOf(Color(0xFFF59E0B), Color(0xFFD97706))
)

val StopGradient = Brush.linearGradient(
    listOf(Color(0xFFF43F5E), Color(0xFFE11D48))
)

val CardGlowGradient = Brush.verticalGradient(
    listOf(Color(0x266366F1), Color(0x006366F1))
)

val CalmAuraGradient = Brush.radialGradient(
    listOf(Color(0x336366F1), Color(0x15F59E0B), Color(0x00000000))
)
