package com.pawspeace.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawspeace.ui.theme.AccentAmber
import com.pawspeace.ui.theme.AccentAmberLight
import com.pawspeace.ui.theme.AccentCyan
import com.pawspeace.ui.theme.AccentCyanLight
import com.pawspeace.ui.theme.AccentEmerald
import com.pawspeace.ui.theme.AccentIndigo
import com.pawspeace.ui.theme.AccentIndigoLight
import com.pawspeace.ui.theme.AccentRose
import com.pawspeace.ui.theme.AccentRoseLight
import com.pawspeace.ui.theme.BgCard
import com.pawspeace.ui.theme.BgCardElevated
import com.pawspeace.ui.theme.BorderSubtle
import com.pawspeace.ui.theme.TextMuted
import com.pawspeace.ui.theme.TextPrimary
import com.pawspeace.ui.theme.TextSecondary

data class SoundscapeOption(
    val id: String,
    val emoji: String,
    val title: String,
    val description: String,
    val themeColor: Color
)

@Composable
fun SoundscapeSection(
    ambianceType: String,
    onAmbianceTypeChange: (String) -> Unit,
    voiceVolume: Float,
    onVoiceVolumeChange: (Float) -> Unit,
    ambianceVolume: Float,
    onAmbianceVolumeChange: (Float) -> Unit
) {
    val soundscapes = listOf(
        SoundscapeOption("rain", "🌧️", "Pink Rain", "Noise & storm shield", AccentCyan),
        SoundscapeOption("heartbeat", "💓", "Heartbeat", "Maternal heartbeat pulse", AccentRose),
        SoundscapeOption("drone", "🎵", "432Hz Drone", "Calm harmonic resonance", AccentIndigo),
        SoundscapeOption("all", "🌊", "Full Blend", "All audio layers combined", AccentAmber)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    listOf(BgCardElevated, BgCard)
                )
            )
            .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {
        // Section Header (Clean, unconstrained)
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎛️ Sensory Soundscapes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Procedural Audio",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentCyanLight,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AccentCyan.copy(alpha = 0.15f))
                        .border(1.dp, AccentCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Scientifically tuned acoustic frequencies for canine ears",
                fontSize = 12.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2x2 Soundscape Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            soundscapes.take(2).forEach { option ->
                SoundscapeCard(
                    option = option,
                    isSelected = ambianceType == option.id,
                    onClick = { onAmbianceTypeChange(option.id) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            soundscapes.drop(2).forEach { option ->
                SoundscapeCard(
                    option = option,
                    isSelected = ambianceType == option.id,
                    onClick = { onAmbianceTypeChange(option.id) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Channel Volume Mixers
        Text(
            text = "VOLUME MIXER CHANNELS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Voice Volume Slider
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(BgCard)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎙️", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Voice Message Volume",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
                Text(
                    text = "${(voiceVolume * 100).toInt()}%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentAmberLight
                )
            }

            Slider(
                value = voiceVolume,
                onValueChange = onVoiceVolumeChange,
                colors = SliderDefaults.colors(
                    thumbColor = AccentAmber,
                    activeTrackColor = AccentAmber,
                    inactiveTrackColor = BorderSubtle
                ),
                modifier = Modifier.height(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Ambiance Volume Slider
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(BgCard)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🌧️", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Background Soundscape",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
                Text(
                    text = "${(ambianceVolume * 100).toInt()}%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentCyanLight
                )
            }

            Slider(
                value = ambianceVolume,
                onValueChange = onAmbianceVolumeChange,
                colors = SliderDefaults.colors(
                    thumbColor = AccentCyan,
                    activeTrackColor = AccentCyan,
                    inactiveTrackColor = BorderSubtle
                ),
                modifier = Modifier.height(30.dp)
            )
        }
    }
}

@Composable
fun SoundscapeCard(
    option: SoundscapeOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) option.themeColor.copy(alpha = 0.15f)
                else BgCard
            )
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) option.themeColor else BorderSubtle,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = option.emoji, fontSize = 22.sp)
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .background(option.themeColor, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = option.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) option.themeColor else TextPrimary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = option.description,
                fontSize = 11.sp,
                color = TextMuted,
                lineHeight = 14.sp
            )
        }
    }
}
