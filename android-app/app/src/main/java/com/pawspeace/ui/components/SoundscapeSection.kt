package com.pawspeace.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawspeace.ui.theme.AccentAmber
import com.pawspeace.ui.theme.AccentCyan
import com.pawspeace.ui.theme.AccentEmerald
import com.pawspeace.ui.theme.AccentIndigo
import com.pawspeace.ui.theme.AccentRose
import com.pawspeace.ui.theme.BgCard
import com.pawspeace.ui.theme.BorderSubtle
import com.pawspeace.ui.theme.TextPrimary
import com.pawspeace.ui.theme.TextSecondary

@Composable
fun SoundscapeSection(
    ambianceType: String,
    onAmbianceTypeChange: (String) -> Unit,
    voiceVolume: Float,
    onVoiceVolumeChange: (Float) -> Unit,
    ambianceVolume: Float,
    onAmbianceVolumeChange: (Float) -> Unit
) {
    val presets = listOf(
        Triple("rain", "🌧 Gentle Rain", AccentCyan),
        Triple("heartbeat", "💓 Heartbeat", AccentRose),
        Triple("drone", "🎶 432Hz Drone", AccentIndigo),
        Triple("all", "🌊 Full Blend", AccentAmber)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(BgCard)
            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Text(
            text = "🎛 Background Ambiance",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Sound Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presets.forEach { (id, label, color) ->
                val isSelected = ambianceType == id
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) color.copy(alpha = 0.15f) else BgCard)
                        .border(1.dp, if (isSelected) color else BorderSubtle, RoundedCornerShape(12.dp))
                        .clickable { onAmbianceTypeChange(id) }
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) color else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sliders
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Voice Volume", fontSize = 12.sp, color = TextSecondary)
            Text("${(voiceVolume * 100).toInt()}%", fontSize = 12.sp, color = AccentAmber, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = voiceVolume,
            onValueChange = onVoiceVolumeChange,
            colors = SliderDefaults.colors(thumbColor = AccentAmber, activeTrackColor = AccentAmber)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Ambiance Volume", fontSize = 12.sp, color = TextSecondary)
            Text("${(ambianceVolume * 100).toInt()}%", fontSize = 12.sp, color = AccentCyan, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = ambianceVolume,
            onValueChange = onAmbianceVolumeChange,
            colors = SliderDefaults.colors(thumbColor = AccentCyan, activeTrackColor = AccentCyan)
        )
    }
}
