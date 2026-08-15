package com.pawspeace.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.pawspeace.ui.theme.AccentEmerald
import com.pawspeace.ui.theme.AccentEmeraldGlow
import com.pawspeace.ui.theme.AccentIndigo
import com.pawspeace.ui.theme.AccentIndigoGlow
import com.pawspeace.ui.theme.AccentIndigoLight
import com.pawspeace.ui.theme.AccentRose
import com.pawspeace.ui.theme.AccentRoseGlow
import com.pawspeace.ui.theme.BgCard
import com.pawspeace.ui.theme.BgCardElevated
import com.pawspeace.ui.theme.BorderAccent
import com.pawspeace.ui.theme.BorderSubtle
import com.pawspeace.ui.theme.TextDark
import com.pawspeace.ui.theme.TextMuted
import com.pawspeace.ui.theme.TextPrimary
import com.pawspeace.ui.theme.TextSecondary

data class PresetPhrase(val emoji: String, val title: String, val text: String)

@Composable
fun PhraseEditorSection(
    phraseText: String,
    onPhraseTextChange: (String) -> Unit,
    voiceMode: String,
    onVoiceModeChange: (String) -> Unit,
    isGenerating: Boolean,
    isRecording: Boolean,
    onGenerateClicked: () -> Unit,
    onRecordClicked: () -> Unit,
    hasApiKey: Boolean
) {
    val presets = listOf(
        PresetPhrase("🐾", "Safe & Loved", "Good boy, you are safe and loved. Rest now."),
        PresetPhrase("🚪", "Leaving Home", "I'll be back real soon buddy. Stay cozy and sleep well."),
        PresetPhrase("🌙", "Bedtime", "Sweet dreams buddy. Close your eyes and relax."),
        PresetPhrase("⚡", "Storm Calm", "Thunder is just harmless outside noise. You're protected.")
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
        // 1. Header: Title & Subtitle (Full width, clean)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "🎙️ Comforting Voice Message",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Reassuring audio loop played at scheduled intervals",
                fontSize = 12.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Full-Width Segmented Tab Bar (Guaranteed equal 50/50 split, never squished!)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(BgCard)
                .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
                .padding(4.dp)
        ) {
            // Tab 1: AI Voice
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(11.dp))
                    .background(
                        if (voiceMode == "ai") Brush.linearGradient(listOf(AccentIndigo, Color(0xFF4F46E5)))
                        else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                    )
                    .clickable { onVoiceModeChange("ai") }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("✨", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "AI Voice Synthesis",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (voiceMode == "ai") TextPrimary else TextSecondary
                    )
                }
            }

            // Tab 2: Record Voice
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(11.dp))
                    .background(
                        if (voiceMode == "record") Brush.linearGradient(listOf(AccentAmber, Color(0xFFD97706)))
                        else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                    )
                    .clickable { onVoiceModeChange("record") }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🎙️", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Owner Voice Studio",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (voiceMode == "record") TextDark else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (voiceMode == "ai") {
            // Quick Reassurance Preset Chips
            Text(
                text = "QUICK REASSURANCE PRESETS",
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
                presets.forEach { preset ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(AccentIndigoGlow.copy(alpha = 0.15f))
                            .border(1.dp, BorderAccent.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .clickable {
                                onPhraseTextChange(
                                    if (phraseText.isBlank()) preset.text
                                    else "$phraseText ${preset.text}"
                                )
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = preset.emoji, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = preset.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AccentIndigoLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Phrase Text Input Field
            OutlinedTextField(
                value = phraseText,
                onValueChange = onPhraseTextChange,
                placeholder = {
                    Text(
                        "Enter reassuring words for your dog (e.g., 'Good boy, I\\'ll be back soon...')...",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                },
                minLines = 3,
                maxLines = 4,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BgCardElevated,
                    unfocusedContainerColor = BgCard,
                    focusedBorderColor = AccentIndigo,
                    unfocusedBorderColor = BorderSubtle,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = AccentAmber
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Action Bar: Engine Badge on Left + Generate Button on Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Engine Status Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (hasApiKey) AccentEmeraldGlow.copy(alpha = 0.15f) else AccentAmberGlow.copy(alpha = 0.15f))
                        .border(
                            1.dp,
                            if (hasApiKey) AccentEmerald.copy(alpha = 0.4f) else AccentAmber.copy(alpha = 0.4f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 9.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(if (hasApiKey) AccentEmerald else AccentAmber, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (hasApiKey) "ElevenLabs Active" else "Native TTS Engine",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (hasApiKey) AccentEmerald else AccentAmber
                    )
                }

                // Generate Button
                Button(
                    onClick = onGenerateClicked,
                    enabled = !isGenerating && phraseText.isNotBlank(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentAmber,
                        contentColor = TextDark,
                        disabledContainerColor = BgCardElevated,
                        disabledContentColor = TextMuted
                    ),
                    modifier = Modifier.height(42.dp)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            color = TextDark,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Synthesizing...", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Text("⚡ Generate Audio", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Owner Voice Studio View
            val recordTransition = rememberInfiniteTransition(label = "recording_pulse")
            val recordPulse by recordTransition.animateFloat(
                initialValue = 1.0f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 800),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "recordPulse"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(BgCard)
                    .border(1.dp, if (isRecording) AccentRoseGlow else BorderSubtle, RoundedCornerShape(18.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Recording Mic Icon / Pulsing Circle
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .scale(if (isRecording) recordPulse else 1.0f)
                        .background(
                            if (isRecording) AccentRoseGlow else AccentIndigoGlow.copy(alpha = 0.25f),
                            CircleShape
                        )
                        .border(
                            2.dp,
                            if (isRecording) AccentRose else BorderSubtle,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isRecording) "🎙️" else "🎤",
                        fontSize = 34.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (isRecording) "Recording Your Voice..." else "Record Owner's Real Voice",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isRecording) AccentRose else TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isRecording) "Speak in a calm, soothing tone..." else "Dogs respond with 40% lower stress to their owner's voice",
                    fontSize = 12.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Start/Stop Record Button
                Button(
                    onClick = onRecordClicked,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRecording) AccentRose else AccentAmber,
                        contentColor = if (isRecording) TextPrimary else TextDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(48.dp)
                ) {
                    Text(
                        text = if (isRecording) "⏹ Stop & Save Voice" else "🎙️ Start Voice Recording",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
