package com.pawspeace.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.pawspeace.ui.theme.AccentAmberGlow
import com.pawspeace.ui.theme.AccentAmberLight
import com.pawspeace.ui.theme.AccentCyan
import com.pawspeace.ui.theme.AccentEmerald
import com.pawspeace.ui.theme.AccentEmeraldGlow
import com.pawspeace.ui.theme.AccentEmeraldLight
import com.pawspeace.ui.theme.AccentIndigo
import com.pawspeace.ui.theme.AccentIndigoLight
import com.pawspeace.ui.theme.BgCard
import com.pawspeace.ui.theme.BgCardElevated
import com.pawspeace.ui.theme.BgDark
import com.pawspeace.ui.theme.BorderSubtle
import com.pawspeace.ui.theme.HeroPlayGradient
import com.pawspeace.ui.theme.StopGradient
import com.pawspeace.ui.theme.TextDark
import com.pawspeace.ui.theme.TextMuted
import com.pawspeace.ui.theme.TextPrimary
import com.pawspeace.ui.theme.TextSecondary
import com.pawspeace.viewmodel.HomeViewModel

data class QuickPresetItem(
    val id: String,
    val emoji: String,
    val title: String,
    val durationText: String,
    val themeColor: Color
)

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showSettings by remember { mutableStateOf(false) }

    val quickPresets = listOf(
        QuickPresetItem("thunder", "⛈️", "Thunder Shield", "Pink Rain • 60m", AccentCyan),
        QuickPresetItem("bedtime", "🌙", "Bedtime Sleep", "432Hz Drone • 2h", AccentIndigoLight),
        QuickPresetItem("leaving", "🚪", "Leaving Home", "5m Delay • 30m", AccentAmber),
        QuickPresetItem("quick_nap", "🧘", "Quick Relax", "432Hz Drone • 15m", AccentEmeraldLight)
    )

    Scaffold(
        containerColor = BgDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFF0F172A),
                            BgDark,
                            BgDark
                        )
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(BgCardElevated, BgCard)
                        )
                    )
                    .border(1.dp, BorderSubtle, RoundedCornerShape(22.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pet Avatar & Brand Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(AccentAmber, AccentIndigo)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🐾", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Paws & Peace",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "For ${state.petName.ifBlank { "Buddy" }}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AccentAmberLight
                        )
                    }
                }

                // Settings Trigger Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(BgDark)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                        .clickable { showSettings = true }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚙️ Settings",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(
                                if (state.apiKey.isNotBlank()) AccentEmerald else AccentAmber,
                                CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hero Breathing Visualizer Centerpiece
            BreathingVisualizer(
                petName = state.petName,
                isPlaying = state.isPlaying
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Radar Status Badge
            val pulseTransition = rememberInfiniteTransition(label = "status_pulse")
            val radarScale by pulseTransition.animateFloat(
                initialValue = 0.85f,
                targetValue = 1.15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "radarScale"
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (state.isPlaying) AccentEmeraldGlow.copy(alpha = 0.15f)
                        else BgCard
                    )
                    .border(
                        1.dp,
                        if (state.isPlaying) AccentEmerald.copy(alpha = 0.4f) else BorderSubtle,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(if (state.isPlaying) radarScale else 1.0f)
                        .background(
                            if (state.isPlaying) AccentEmerald else TextMuted,
                            CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = state.statusText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.isPlaying) AccentEmeraldLight else TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Hero Play / Stop Action Button (Polished pill with glowing gradient)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        if (state.isPlaying) StopGradient else HeroPlayGradient
                    )
                    .border(
                        1.dp,
                        if (state.isPlaying) Color(0xFFFF8096) else AccentAmberLight.copy(alpha = 0.5f),
                        RoundedCornerShape(18.dp)
                    )
                    .clickable {
                        if (state.isPlaying) {
                            viewModel.stopPlayback(context)
                        } else {
                            viewModel.startPlayback(context)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (state.isPlaying) "⏹" else "▶",
                        fontSize = 16.sp,
                        color = if (state.isPlaying) TextPrimary else TextDark
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (state.isPlaying) "Stop Calming Session" else "Start Calming Session",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.3.sp,
                        color = if (state.isPlaying) TextPrimary else TextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // One-Tap Quick Presets Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "ONE-TAP CALMING PRESETS",
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
                    quickPresets.forEach { preset ->
                        val isPresetActive = state.activePreset == preset.id
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isPresetActive) preset.themeColor.copy(alpha = 0.2f)
                                    else BgCard
                                )
                                .border(
                                    1.dp,
                                    if (isPresetActive) preset.themeColor else BorderSubtle,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable {
                                    viewModel.applyPreset(preset.id)
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = preset.emoji, fontSize = 15.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = preset.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPresetActive) preset.themeColor else TextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = preset.durationText,
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Phrase Editor & Recording Section
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

            // Soundscape Multi-track Mixer Section
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

            // Footer
            Text(
                text = "🐾 Paws & Peace — Keeping Pups Calm & Anxiety-Free",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Dual-Platform Sensory Audio Engine • v1.0",
                fontSize = 10.sp,
                color = TextMuted.copy(alpha = 0.5f)
            )
        }
    }

    if (showSettings) {
        SettingsDialog(
            currentApiKey = state.apiKey,
            currentVoiceId = state.voiceId,
            currentStability = state.stability,
            currentSimilarity = state.similarity,
            petName = state.petName,
            onPetNameChange = { viewModel.updatePetName(it) },
            onDismiss = { showSettings = false },
            onSave = { key, voice, stab, sim ->
                viewModel.saveSettings(key, voice, stab, sim)
            }
        )
    }
}
