package com.pawspeace.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pawspeace.audio.VoiceRecorderHelper
import com.pawspeace.data.ElevenLabsApiClient
import com.pawspeace.data.PreferencesManager
import com.pawspeace.data.TTSRequest
import com.pawspeace.data.VoiceSettings
import com.pawspeace.service.PlaybackService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

data class HomeUiState(
    val petName: String = "Buddy",
    val apiKey: String = "",
    val voiceId: String = "21m00Tcm4TlvDq8ikWAM",
    val stability: Float = 0.75f,
    val similarity: Float = 0.85f,
    val phraseText: String = "Good boy Buddy, I'll be back real soon. You are safe.",
    val voiceMode: String = "ai",
    val isGenerating: Boolean = false,
    val isRecording: Boolean = false,
    val isPlaying: Boolean = false,
    val ambianceType: String = "rain",
    val voiceVolume: Float = 0.9f,
    val ambianceVolume: Float = 0.4f,
    val durationMinutes: Int = 30,
    val repeatMinutes: Int = 3,
    val delayMinutes: Int = 0,
    val statusText: String = "Ready to soothe 🐾",
    val generatedAudioUri: Uri? = null
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)
    private val voiceRecorder = VoiceRecorderHelper(application)
    private var nativeTts: TextToSpeech? = null

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Init native TTS
        nativeTts = TextToSpeech(application) { status ->
            if (status == TextToSpeech.SUCCESS) {
                nativeTts?.language = Locale.US
                nativeTts?.setSpeechRate(0.85f)
            }
        }

        // Load saved preferences
        viewModelScope.launch {
            val key = prefs.apiKeyFlow.first()
            val name = prefs.petNameFlow.first()
            val voice = prefs.voiceIdFlow.first()
            val stab = prefs.stabilityFlow.first()
            val sim = prefs.similarityFlow.first()

            _uiState.value = _uiState.value.copy(
                apiKey = key,
                petName = name,
                voiceId = voice,
                stability = stab,
                similarity = sim
            )
        }
    }

    fun updatePetName(name: String) {
        _uiState.value = _uiState.value.copy(petName = name)
        viewModelScope.launch { prefs.savePetName(name) }
    }

    fun updatePhraseText(text: String) {
        _uiState.value = _uiState.value.copy(phraseText = text)
    }

    fun updateVoiceMode(mode: String) {
        _uiState.value = _uiState.value.copy(voiceMode = mode)
    }

    fun updateAmbianceType(type: String) {
        _uiState.value = _uiState.value.copy(ambianceType = type)
    }

    fun updateVoiceVolume(vol: Float) {
        _uiState.value = _uiState.value.copy(voiceVolume = vol)
    }

    fun updateAmbianceVolume(vol: Float) {
        _uiState.value = _uiState.value.copy(ambianceVolume = vol)
    }

    fun updateDuration(mins: Int) {
        _uiState.value = _uiState.value.copy(durationMinutes = mins)
    }

    fun updateRepeat(mins: Int) {
        _uiState.value = _uiState.value.copy(repeatMinutes = mins)
    }

    fun updateDelay(mins: Int) {
        _uiState.value = _uiState.value.copy(delayMinutes = mins)
    }

    fun saveSettings(key: String, voiceId: String, stab: Float, sim: Float) {
        _uiState.value = _uiState.value.copy(
            apiKey = key,
            voiceId = voiceId,
            stability = stab,
            similarity = sim
        )
        viewModelScope.launch {
            prefs.saveApiKey(key)
            prefs.saveVoiceSettings(voiceId, stab, sim)
        }
    }

    fun generateVoiceTrack() {
        val state = _uiState.value
        _uiState.value = state.copy(isGenerating = true)

        viewModelScope.launch {
            try {
                if (state.apiKey.isNotBlank()) {
                    // Call ElevenLabs API
                    val response = ElevenLabsApiClient.service.generateSpeech(
                        apiKey = state.apiKey,
                        voiceId = state.voiceId,
                        request = TTSRequest(
                            text = state.phraseText,
                            voiceSettings = VoiceSettings(stability = state.stability, similarityBoost = state.similarity)
                        )
                    )

                    if (response.isSuccessful && response.body() != null) {
                        val file = File(getApplication<Application>().cacheDir, "elevenlabs_voice.mp3")
                        FileOutputStream(file).use { output ->
                            response.body()!!.byteStream().copyTo(output)
                        }
                        _uiState.value = _uiState.value.copy(
                            generatedAudioUri = Uri.fromFile(file),
                            isGenerating = false,
                            statusText = "Voice track ready! 🐾"
                        )
                        return@launch
                    }
                }

                // Fallback to Native TTS
                nativeTts?.speak(state.phraseText, TextToSpeech.QUEUE_FLUSH, null, "tts_id")
                _uiState.value = _uiState.value.copy(isGenerating = false, statusText = "Speaking via Native TTS 🔊")
            } catch (e: Exception) {
                nativeTts?.speak(state.phraseText, TextToSpeech.QUEUE_FLUSH, null, "tts_id")
                _uiState.value = _uiState.value.copy(isGenerating = false, statusText = "Fallback TTS active 🔊")
            }
        }
    }

    fun toggleRecording() {
        val isRecording = _uiState.value.isRecording
        if (!isRecording) {
            voiceRecorder.startRecording(
                onSuccess = { _uiState.value = _uiState.value.copy(isRecording = true, statusText = "Recording your voice... 🎙") },
                onError = { _uiState.value = _uiState.value.copy(statusText = "Microphone error") }
            )
        } else {
            val file = voiceRecorder.stopRecording()
            if (file != null) {
                _uiState.value = _uiState.value.copy(
                    isRecording = false,
                    generatedAudioUri = Uri.fromFile(file),
                    statusText = "Owner voice recorded! 🐾"
                )
            }
        }
    }

    fun startPlayback(context: Context) {
        _uiState.value = _uiState.value.copy(isPlaying = true, statusText = "Active: Soothing pup 🐾")
        val intent = Intent(context, PlaybackService::class.java).apply {
            action = PlaybackService.ACTION_START
            putExtra(PlaybackService.EXTRA_VOICE_URI, _uiState.value.generatedAudioUri?.toString())
            putExtra(PlaybackService.EXTRA_DURATION_MIN, _uiState.value.durationMinutes)
            putExtra(PlaybackService.EXTRA_REPEAT_MIN, _uiState.value.repeatMinutes)
            putExtra(PlaybackService.EXTRA_AMBIANCE_TYPE, _uiState.value.ambianceType)
            putExtra(PlaybackService.EXTRA_DELAY_MIN, _uiState.value.delayMinutes)
        }
        context.startService(intent)
    }

    fun stopPlayback(context: Context) {
        _uiState.value = _uiState.value.copy(isPlaying = false, statusText = "Ready to soothe 🐾")
        val intent = Intent(context, PlaybackService::class.java).apply {
            action = PlaybackService.ACTION_STOP
        }
        context.startService(intent)
    }

    override fun onCleared() {
        nativeTts?.stop()
        nativeTts?.shutdown()
        super.onCleared()
    }
}
