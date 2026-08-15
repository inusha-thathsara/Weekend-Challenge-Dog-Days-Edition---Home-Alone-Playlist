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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pawspeace.data.PRESET_VOICES
import com.pawspeace.ui.theme.AccentAmber
import com.pawspeace.ui.theme.AccentIndigo
import com.pawspeace.ui.theme.BgCard
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
    onDismiss: () -> Unit,
    onSave: (apiKey: String, voiceId: String, stability: Float, similarity: Float) -> Unit
) {
    var apiKey by remember { mutableStateOf(currentApiKey) }
    var selectedVoiceId by remember { mutableStateOf(currentVoiceId) }
    var stability by remember { mutableFloatStateOf(currentStability) }
    var similarity by remember { mutableFloatStateOf(currentSimilarity) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = BgCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "ElevenLabs Voice Settings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Configure API Key & Voice Preset",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(16.dp))

                // API Key Field
                Text(text = "API Key", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    placeholder = { Text("xi-api-key...", color = TextMuted, fontSize = 13.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentIndigo,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Voice Preset Selection
                Text(text = "Soothing Voice", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(modifier = Modifier.height(130.dp)) {
                    items(PRESET_VOICES) { voice ->
                        val isSelected = voice.id == selectedVoiceId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .background(
                                    if (isSelected) AccentIndigo.copy(alpha = 0.2f) else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) AccentIndigo else BorderSubtle,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedVoiceId = voice.id }
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = voice.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                Text(text = voice.description, fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stability Slider
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Voice Stability", fontSize = 12.sp, color = TextSecondary)
                    Text(text = "%.2f".format(stability), fontSize = 12.sp, color = AccentIndigo, fontWeight = FontWeight.Bold)
                }
                Slider(
                    value = stability,
                    onValueChange = { stability = it },
                    valueRange = 0.3f..1.0f,
                    colors = SliderDefaults.colors(thumbColor = AccentAmber, activeTrackColor = AccentIndigo)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Button(
                        onClick = {
                            onSave(apiKey, selectedVoiceId, stability, similarity)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentIndigo)
                    ) {
                        Text("Save Settings")
                    }
                }
            }
        }
    }
}
