package com.pawspeace.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawspeace.ui.components.BreathingVisualizer
import com.pawspeace.ui.components.PhraseEditorSection
import com.pawspeace.ui.components.SettingsDialog
import com.pawspeace.ui.components.SoundscapeSection
import com.pawspeace.ui.components.TimerSection
import com.pawspeace.ui.theme.AccentAmber
import com.pawspeace.ui.theme.AccentEmerald
import com.pawspeace.ui.theme.AccentIndigo
import com.pawspeace.ui.theme.AccentRose
import com.pawspeace.ui.theme.BgCard
import com.pawspeace.ui.theme.BgDark
import com.pawspeace.ui.theme.BorderSubtle
import com.pawspeace.ui.theme.TextMuted
import com.pawspeace.ui.theme.TextPrimary
import com.pawspeace.ui.theme.TextSecondary
import com.pawspeace.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showSettings by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BgDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(BgCard)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                Brush.linearGradient(listOf(AccentAmber, AccentIndigo)),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🐾", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column {
                        Text("Paws & Peace", fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = TextPrimary)
                        Text("Home Alone Playlist", fontSize = 11.sp, color = TextMuted)
                    }
                }

                // Settings Button
                Row(
                    modifier = Modifier
                        .background(BgDark, RoundedCornerShape(12.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                        .clickable { showSettings = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⚙ Settings", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                    Spacer(modifier = Modifier.size(6.dp))
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(if (state.apiKey.isNotBlank()) AccentEmerald else AccentAmber, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Breathing Visualizer Hero
            BreathingVisualizer(
                petName = state.petName,
                isPlaying = state.isPlaying
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = state.statusText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = AccentEmerald
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Main Play/Stop Button
            Button(
                onClick = {
                    if (state.isPlaying) {
                        viewModel.stopPlayback(context)
                    } else {
                        viewModel.startPlayback(context)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.isPlaying) AccentRose else AccentAmber,
                    contentColor = if (state.isPlaying) TextPrimary else androidx.compose.ui.graphics.Color.Black
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = if (state.isPlaying) "⏹ Stop Soothing Playlist" else "▶ Start Soothing Playlist",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Phrase Editor Section
            PhraseEditorSection(
                phraseText = state.phraseText,
                onPhraseTextChange = { viewModel.updatePhraseText(it) },
                voiceMode = state.voiceMode,
                onVoiceModeChange = { viewModel.updateVoiceMode(it) },
                isGenerating = state.isGenerating,
                isRecording = state.isRecording,
                onGenerateClicked = { viewModel.generateVoiceTrack() },
                onRecordClicked = { viewModel.toggleRecording() },
                hasApiKey = state.apiKey.isNotBlank()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Soundscape Mixer Section
            SoundscapeSection(
                ambianceType = state.ambianceType,
                onAmbianceTypeChange = { viewModel.updateAmbianceType(it) },
                voiceVolume = state.voiceVolume,
                onVoiceVolumeChange = { viewModel.updateVoiceVolume(it) },
                ambianceVolume = state.ambianceVolume,
                onAmbianceVolumeChange = { viewModel.updateAmbianceVolume(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Timer & Scheduler Section
            TimerSection(
                durationMinutes = state.durationMinutes,
                onDurationChange = { viewModel.updateDuration(it) },
                repeatMinutes = state.repeatMinutes,
                onRepeatChange = { viewModel.updateRepeat(it) },
                delayMinutes = state.delayMinutes,
                onDelayChange = { viewModel.updateDelay(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "🐾 Paws & Peace — Keeping pups calm & relaxed",
                fontSize = 11.sp,
                color = TextMuted
            )
        }
    }

    if (showSettings) {
        SettingsDialog(
            currentApiKey = state.apiKey,
            currentVoiceId = state.voiceId,
            currentStability = state.stability,
            currentSimilarity = state.similarity,
            onDismiss = { showSettings = false },
            onSave = { key, voice, stab, sim ->
                viewModel.saveSettings(key, voice, stab, sim)
            }
        )
    }
}
