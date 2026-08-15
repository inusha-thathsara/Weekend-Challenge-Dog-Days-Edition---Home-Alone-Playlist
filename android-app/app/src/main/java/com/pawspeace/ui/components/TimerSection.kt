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
import com.pawspeace.ui.theme.AccentEmerald
import com.pawspeace.ui.theme.AccentIndigo
import com.pawspeace.ui.theme.BgCard
import com.pawspeace.ui.theme.BorderSubtle
import com.pawspeace.ui.theme.TextMuted
import com.pawspeace.ui.theme.TextPrimary
import com.pawspeace.ui.theme.TextSecondary

@Composable
fun TimerSection(
    durationMinutes: Int,
    onDurationChange: (Int) -> Unit,
    repeatMinutes: Int,
    onRepeatChange: (Int) -> Unit,
    delayMinutes: Int,
    onDelayChange: (Int) -> Unit
) {
    val durationOptions = listOf(
        15 to "15m",
        30 to "30m",
        60 to "1h",
        120 to "2h",
        0 to "Loop ♾"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(BgCard)
            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⏱ Timer & Scheduler",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Auto Fade-out",
                fontSize = 10.sp,
                color = AccentEmerald,
                modifier = Modifier
                    .background(AccentEmerald.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Duration Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            durationOptions.forEach { (mins, label) ->
                val isSelected = durationMinutes == mins
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) AccentEmerald else TextMuted,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) AccentEmerald.copy(alpha = 0.15f) else BgCard)
                        .border(1.dp, if (isSelected) AccentEmerald else BorderSubtle, RoundedCornerShape(10.dp))
                        .clickable { onDurationChange(mins) }
                        .padding(vertical = 8.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Repeat Slider
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Repeat Voice Clip", fontSize = 12.sp, color = TextSecondary)
            Text("Every $repeatMinutes min", fontSize = 12.sp, color = AccentIndigo, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = repeatMinutes.toFloat(),
            onValueChange = { onRepeatChange(it.toInt()) },
            valueRange = 1f..10f,
            steps = 8,
            colors = SliderDefaults.colors(thumbColor = AccentIndigo, activeTrackColor = AccentIndigo)
        )

        // Delay Slider
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Start Delay", fontSize = 12.sp, color = TextSecondary)
            Text(if (delayMinutes == 0) "Immediate" else "$delayMinutes min delay", fontSize = 12.sp, color = AccentAmber, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = delayMinutes.toFloat(),
            onValueChange = { onDelayChange(it.toInt()) },
            valueRange = 0f..30f,
            steps = 5,
            colors = SliderDefaults.colors(thumbColor = AccentAmber, activeTrackColor = AccentAmber)
        )
    }
}
