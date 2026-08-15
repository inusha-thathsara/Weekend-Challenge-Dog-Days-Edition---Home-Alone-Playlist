package com.pawspeace.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import com.pawspeace.ui.theme.AccentEmerald
import com.pawspeace.ui.theme.AccentEmeraldGlow
import com.pawspeace.ui.theme.AccentEmeraldLight
import com.pawspeace.ui.theme.AccentIndigo
import com.pawspeace.ui.theme.AccentIndigoLight
import com.pawspeace.ui.theme.BgCard
import com.pawspeace.ui.theme.BgCardElevated
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
        240 to "4h",
        0 to "Loop ♾️"
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
                    text = "⏱️ Session Scheduler",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Auto Fade-out",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentEmeraldLight,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AccentEmeraldGlow.copy(alpha = 0.15f))
                        .border(1.dp, AccentEmerald.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Session duration & reassuring voice repeat intervals",
                fontSize = 12.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Session Duration Chips
        Text(
            text = "TOTAL PLAYBACK DURATION",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            durationOptions.forEach { (mins, label) ->
                val isSelected = durationMinutes == mins
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) AccentEmeraldGlow.copy(alpha = 0.25f)
                            else BgCard
                        )
                        .border(
                            1.dp,
                            if (isSelected) AccentEmerald else BorderSubtle,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onDurationChange(mins) }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                        color = if (isSelected) AccentEmeraldLight else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Voice Repeat Loop Slider
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
                Column(modifier = Modifier.weight(1f)) {
                    Text("🔁 Reassure Voice Loop", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Re-plays phrase every $repeatMinutes min", fontSize = 11.sp, color = TextMuted)
                }
                Text(
                    text = "Every ${repeatMinutes}m",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentIndigoLight
                )
            }

            Slider(
                value = repeatMinutes.toFloat(),
                onValueChange = { onRepeatChange(it.toInt()) },
                valueRange = 1f..10f,
                steps = 8,
                colors = SliderDefaults.colors(
                    thumbColor = AccentIndigo,
                    activeTrackColor = AccentIndigo,
                    inactiveTrackColor = BorderSubtle
                ),
                modifier = Modifier.height(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Departure Delay Slider
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
                Column(modifier = Modifier.weight(1f)) {
                    Text("🚪 Departure Delay Start", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Time to leave before audio begins", fontSize = 11.sp, color = TextMuted)
                }
                Text(
                    text = if (delayMinutes == 0) "Immediate" else "${delayMinutes}m delay",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentAmberLight
                )
            }

            Slider(
                value = delayMinutes.toFloat(),
                onValueChange = { onDelayChange(it.toInt()) },
                valueRange = 0f..30f,
                steps = 5,
                colors = SliderDefaults.colors(
                    thumbColor = AccentAmber,
                    activeTrackColor = AccentAmber,
                    inactiveTrackColor = BorderSubtle
                ),
                modifier = Modifier.height(30.dp)
            )
        }
    }
}
