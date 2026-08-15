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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pawspeace.data.PRESET_VOICES
import com.pawspeace.ui.theme.AccentAmber
import com.pawspeace.ui.theme.AccentAmberLight
import com.pawspeace.ui.theme.AccentEmerald
import com.pawspeace.ui.theme.AccentIndigo
import com.pawspeace.ui.theme.AccentIndigoGlow
import com.pawspeace.ui.theme.AccentIndigoLight
import com.pawspeace.ui.theme.BgCard
import com.pawspeace.ui.theme.BgCardElevated
import com.pawspeace.ui.theme.BorderSubtle
import com.pawspeace.ui.theme.TextMuted
import com.pawspeace.ui.theme.TextPrimary
import com.pawspeace.ui.theme.TextSecondary

@Composable
fun SettingsDialog(
    currentApiKey: String,
    currentVoiceId: String,
    currentStability: Float,
    currentSimilarity: Float,
    petName: String = "Buddy",
    onPetNameChange: (String) -> Unit = {},
    onDismiss: () -> Unit,
    onSave: (apiKey: String, voiceId: String, stability: Float, similarity: Float) -> Unit
) {
    var apiKey by remember(currentApiKey) { mutableStateOf(currentApiKey) }
    var nameInput by remember(petName) { mutableStateOf(petName) }
    var selectedVoiceId by remember(currentVoiceId) { mutableStateOf(currentVoiceId) }
    var stability by remember(currentStability) { mutableFloatStateOf(currentStability) }
    var similarity by remember(currentSimilarity) { mutableFloatStateOf(currentSimilarity) }
    var showApiKey by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = BgCardElevated,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Dialog Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Settings & Voice Studio",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Configure Pet Profile & ElevenLabs AI",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AccentIndigoGlow)
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✕", color = TextSecondary, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Section 1: Pet Profile
                Text(
                    text = "🐶 PET PROFILE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = AccentAmberLight
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = {
                        nameInput = it
                        onPetNameChange(it)
                    },
                    label = { Text("Pet Name", color = TextMuted, fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = BgCard,
                        unfocusedContainerColor = BgCard,
                        focusedBorderColor = AccentAmber,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Section 2: ElevenLabs API Key
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔑 ELEVENLABS API KEY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = AccentIndigoLight
                    )

                    Text(
                        text = if (showApiKey) "Hide" else "Show",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AccentIndigoLight,
                        modifier = Modifier.clickable { showApiKey = !showApiKey }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    placeholder = { Text("Paste ElevenLabs API Key...", color = TextMuted, fontSize = 12.sp) },
                    visualTransformation = if (showApiKey) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = BgCard,
                        unfocusedContainerColor = BgCard,
                        focusedBorderColor = AccentIndigo,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Section 3: Voice Preset Selection
                Text(
                    text = "🎙️ SOOTHING AI VOICE MODEL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = AccentIndigoLight
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PRESET_VOICES.forEach { voice ->
                        val isSelected = voice.id == selectedVoiceId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected) AccentIndigoGlow.copy(alpha = 0.25f)
                                    else BgCard
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) AccentIndigo else BorderSubtle,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { selectedVoiceId = voice.id }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = voice.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (isSelected) AccentIndigoLight else TextPrimary
                                    )
                                    if (voice.name == "Rachel") {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Default",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AccentEmerald,
                                            modifier = Modifier
                                                .background(AccentEmerald.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(text = voice.description, fontSize = 11.sp, color = TextMuted)
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .background(AccentIndigo, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✓", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Section 4: Voice Tuning Sliders
                Text(
                    text = "🎚️ VOICE SYNTHESIS TUNING",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = AccentIndigoLight
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Stability
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BgCard)
                        .padding(12.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Voice Stability", fontSize = 12.sp, color = TextSecondary)
                        Text("${(stability * 100).toInt()}%", fontSize = 12.sp, color = AccentIndigoLight, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = stability,
                        onValueChange = { stability = it },
                        valueRange = 0.3f..1.0f,
                        colors = SliderDefaults.colors(thumbColor = AccentIndigo, activeTrackColor = AccentIndigo),
                        modifier = Modifier.height(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Similarity Boost
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BgCard)
                        .padding(12.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Clarity & Similarity Boost", fontSize = 12.sp, color = TextSecondary)
                        Text("${(similarity * 100).toInt()}%", fontSize = 12.sp, color = AccentAmberLight, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = similarity,
                        onValueChange = { similarity = it },
                        valueRange = 0.3f..1.0f,
                        colors = SliderDefaults.colors(thumbColor = AccentAmber, activeTrackColor = AccentAmber),
                        modifier = Modifier.height(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                    ) {
                        Text("Cancel", color = TextMuted, fontSize = 14.sp)
                    }

                    Button(
                        onClick = {
                            onSave(apiKey.trim(), selectedVoiceId, stability, similarity)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentIndigo,
                            contentColor = TextPrimary
                        ),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(46.dp)
                    ) {
                        Text("Save Settings", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
