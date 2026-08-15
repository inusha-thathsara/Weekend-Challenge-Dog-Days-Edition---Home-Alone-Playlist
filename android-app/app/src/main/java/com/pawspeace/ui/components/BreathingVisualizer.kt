package com.pawspeace.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawspeace.ui.theme.AccentAmber
import com.pawspeace.ui.theme.AccentCyan
import com.pawspeace.ui.theme.AccentEmerald
import com.pawspeace.ui.theme.AccentIndigo
import com.pawspeace.ui.theme.AccentRose
import com.pawspeace.ui.theme.TextMuted

@Composable
fun BreathingVisualizer(
    petName: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")

    val scale by infiniteTransition.animateFloat(
        initialValue = if (isPlaying) 0.92f else 1.0f,
        targetValue = if (isPlaying) 1.08f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .size(190.dp)
            .scale(scale)
            .border(
                width = 2.dp,
                color = if (isPlaying) AccentAmber else AccentIndigo.copy(alpha = 0.3f),
                shape = CircleShape
            )
            .background(Color(0xFF0E1424).copy(alpha = 0.8f), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "🐶", fontSize = 38.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = petName.uppercase(),
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentAmber
            )
            Text(
                text = if (isPlaying) "Breathing Rhythm" else "Comfort Zone",
                fontSize = 11.sp,
                color = TextMuted
            )

            if (isPlaying) {
                Spacer(modifier = Modifier.height(8.dp))
                EqualizerBars()
            }
        }
    }
}

@Composable
fun EqualizerBars() {
    val colors = listOf(AccentAmber, AccentIndigo, AccentEmerald, AccentRose, AccentCyan)
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.height(18.dp)
    ) {
        colors.forEachIndexed { index, color ->
            val transition = rememberInfiniteTransition(label = "bar_$index")
            val barHeight by transition.animateFloat(
                initialValue = 4f,
                targetValue = 18f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 600 + index * 120, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "barHeight_$index"
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(barHeight.dp)
                    .background(color, shape = RoundedCornerShape(2.dp))
            )
        }
    }
}
