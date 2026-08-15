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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawspeace.ui.theme.AccentAmber
import com.pawspeace.ui.theme.AccentIndigo
import com.pawspeace.ui.theme.AccentRose
import com.pawspeace.ui.theme.BgCard
import com.pawspeace.ui.theme.BorderSubtle
import com.pawspeace.ui.theme.TextMuted
import com.pawspeace.ui.theme.TextPrimary
import com.pawspeace.ui.theme.TextSecondary

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
        "Good boy, I'll be back soon.",
        "You are safe and loved. Rest now.",
        "Sleep well buddy, see you soon."
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
                text = "✨ Comforting Voice Message",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            // Mode Tabs
            Row(
                modifier = Modifier
                    .background(BgCard, RoundedCornerShape(10.dp))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                    .padding(2.dp)
            ) {
                Text(
                    text = "AI Voice",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (voiceMode == "ai") TextPrimary else TextMuted,
                    modifier = Modifier
                        .background(if (voiceMode == "ai") AccentIndigo else androidx.compose.ui.graphics.Color.Transparent, RoundedCornerShape(8.dp))
                        .clickable { onVoiceModeChange("ai") }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
                Text(
                    text = "Record",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (voiceMode == "record") TextPrimary else TextMuted,
                    modifier = Modifier
                        .background(if (voiceMode == "record") AccentAmber else androidx.compose.ui.graphics.Color.Transparent, RoundedCornerShape(8.dp))
                        .clickable { onVoiceModeChange("record") }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (voiceMode == "ai") {
            // Preset chips
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                presets.forEach { preset ->
                    Text(
                        text = "+ \"${preset.take(18)}...\"",
                        fontSize = 11.sp,
                        color = AccentAmber,
                        modifier = Modifier
                            .background(AccentAmber.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                            .border(1.dp, AccentAmber.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .clickable { onPhraseTextChange(if (phraseText.isEmpty()) preset else "$phraseText $preset") }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = phraseText,
                onValueChange = onPhraseTextChange,
                placeholder = { Text("Enter comforting words for your pup...", color = TextMuted, fontSize = 13.sp) },
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentIndigo,
                    unfocusedBorderColor = BorderSubtle,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (hasApiKey) "✨ ElevenLabs API Ready" else "🔊 Native TTS fallback",
                    fontSize = 11.sp,
                    color = TextMuted
                )

                Button(
                    onClick = onGenerateClicked,
                    enabled = !isGenerating && phraseText.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmber, contentColor = androidx.compose.ui.graphics.Color.Black)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(color = androidx.compose.ui.graphics.Color.Black, modifier = Modifier.height(16.dp))
                    } else {
                        Text("Generate Track", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        } else {
            // Recorder View
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgCard, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🎤 Record Owner's Personal Voice", fontSize = 13.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onRecordClicked,
                    colors = ButtonDefaults.buttonColors(containerColor = if (isRecording) AccentAmber else AccentRose)
                ) {
                    Text(if (isRecording) "⏹ Stop Recording" else "🎙 Start Recording")
                }
            }
        }
    }
}
