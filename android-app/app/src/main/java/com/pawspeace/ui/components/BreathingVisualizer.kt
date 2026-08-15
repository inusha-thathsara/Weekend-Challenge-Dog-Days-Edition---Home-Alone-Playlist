package com.pawspeace.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawspeace.ui.theme.AccentAmber
import com.pawspeace.ui.theme.AccentAmberGlow
import com.pawspeace.ui.theme.AccentAmberLight
import com.pawspeace.ui.theme.AccentCyan
import com.pawspeace.ui.theme.AccentCyanLight
import com.pawspeace.ui.theme.AccentEmerald
import com.pawspeace.ui.theme.AccentEmeraldLight
import com.pawspeace.ui.theme.AccentIndigo
import com.pawspeace.ui.theme.AccentIndigoGlow
import com.pawspeace.ui.theme.AccentIndigoLight
import com.pawspeace.ui.theme.AccentRose
import com.pawspeace.ui.theme.AccentViolet
import com.pawspeace.ui.theme.BgCard
import com.pawspeace.ui.theme.BgCardElevated
import com.pawspeace.ui.theme.BorderMedium
import com.pawspeace.ui.theme.BorderSubtle
import com.pawspeace.ui.theme.TextMuted
import com.pawspeace.ui.theme.TextPrimary

@Composable
fun BreathingVisualizer(
    petName: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing_cycle")

    // Rhythmic breathing scale (Inhale -> Exhale)
    val breathScale by infiniteTransition.animateFloat(
        initialValue = if (isPlaying) 0.94f else 0.98f,
        targetValue = if (isPlaying) 1.06f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathScale"
    )

    // Outer ripple wave expansion (only while playing)
    val outerRippleScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.30f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "outerRippleScale"
    )

    val outerRippleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "outerRippleAlpha"
    )

    val isInhale = breathScale > 1.00f

    Box(
        modifier = modifier
            .size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer Luminous Ripple (playing state)
        if (isPlaying) {
            Box(
                modifier = Modifier
                    .size(175.dp)
                    .scale(outerRippleScale)
                    .border(
                        width = 1.5.dp,
                        color = AccentAmber.copy(alpha = outerRippleAlpha),
                        shape = CircleShape
                    )
            )

            // Secondary Indigo Halo
            Box(
                modifier = Modifier
                    .size(185.dp)
                    .scale(breathScale * 0.98f)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                AccentIndigoGlow.copy(alpha = 0.3f),
                                AccentAmberGlow.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }

        // Main Center Sensory Orb
        Box(
            modifier = Modifier
                .size(175.dp)
                .scale(breathScale)
                .border(
                    width = 2.dp,
                    brush = if (isPlaying) {
                        Brush.sweepGradient(
                            listOf(
                                AccentAmber,
                                AccentIndigoLight,
                                AccentEmeraldLight,
                                AccentCyanLight,
                                AccentAmber
                            )
                        )
                    } else {
                        Brush.linearGradient(listOf(BorderMedium, BorderSubtle))
                    },
                    shape = CircleShape
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            BgCardElevated,
                            BgCard
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(12.dp)
            ) {
                // Pet Avatar Icon with Glowing Ring
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (isPlaying) AccentAmberGlow else AccentIndigoGlow.copy(alpha = 0.25f),
                            CircleShape
                        )
                        .border(
                            1.dp,
                            if (isPlaying) AccentAmberLight else BorderSubtle,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🐶", fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Pet Name Tag
                Text(
                    text = petName.ifBlank { "Buddy" }.uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp,
                    color = if (isPlaying) AccentAmberLight else TextPrimary
                )

                // Rhythmic Breathing Guidance Prompt
                Text(
                    text = if (isPlaying) {
                        if (isInhale) "🌿 Inhale Calm..." else "💨 Exhale Stress..."
                    } else {
                        "Comfort Sanctuary"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isPlaying) AccentEmeraldLight else TextMuted
                )

                // Dynamic Equalizer Bars
                if (isPlaying) {
                    Spacer(modifier = Modifier.height(8.dp))
                    DynamicEqualizerBars()
                }
            }
        }
    }
}

@Composable
fun DynamicEqualizerBars() {
    val barPalette = listOf(
        AccentAmber,
        AccentIndigoLight,
        AccentEmeraldLight,
        AccentRose,
        AccentCyanLight,
        AccentViolet,
        AccentAmberLight
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(3.5.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.height(18.dp)
    ) {
        barPalette.forEachIndexed { index, color ->
            val transition = rememberInfiniteTransition(label = "eq_bar_$index")
            val barHeight by transition.animateFloat(
                initialValue = 4f + (index % 3) * 2f,
                targetValue = 16f - (index % 2) * 2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 420 + (index * 85),
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "barHeight_$index"
            )

            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(barHeight.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(color, color.copy(alpha = 0.5f))
                        ),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}
