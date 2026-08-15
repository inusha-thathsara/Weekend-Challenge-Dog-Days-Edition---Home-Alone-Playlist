package com.pawspeace.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParser
import com.pawspeace.BuildConfig
import com.pawspeace.audio.VoiceRecorderHelper
import com.pawspeace.data.ElevenLabsApiClient
import com.pawspeace.data.PreferencesManager
import com.pawspeace.data.TTSRequest
import com.pawspeace.data.VoiceSettings
import com.pawspeace.service.PlaybackService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    val activePreset: String? = null,
    val generatedAudioUri: Uri? = null
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)
    private val voiceRecorder = VoiceRecorderHelper(application)
    private var nativeTts: TextToSpeech? = null
    private var previewPlayer: MediaPlayer? = null

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Init native TTS as fallback
        nativeTts = TextToSpeech(application) { status ->
            if (status == TextToSpeech.SUCCESS) {
                nativeTts?.language = Locale.US
                nativeTts?.setSpeechRate(0.85f)
            }
        }

        // Load saved preferences & fallback to BuildConfig key if empty
        viewModelScope.launch {
            var key = prefs.apiKeyFlow.first().trim()
            if (key.isBlank() && BuildConfig.DEFAULT_ELEVENLABS_API_KEY.isNotBlank()) {
                key = BuildConfig.DEFAULT_ELEVENLABS_API_KEY.trim()
                prefs.saveApiKey(key)
            }

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
        _uiState.value = _uiState.value.copy(ambianceType = type, activePreset = null)
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

    fun applyPreset(presetId: String) {
        val pet = _uiState.value.petName.ifBlank { "Buddy" }
        when (presetId) {
            "thunder" -> {
                _uiState.value = _uiState.value.copy(
                    activePreset = "thunder",
                    ambianceType = "rain",
                    ambianceVolume = 0.75f,
                    voiceVolume = 0.90f,
                    durationMinutes = 60,
                    repeatMinutes = 2,
                    delayMinutes = 0,
                    phraseText = "Thunder is just harmless noise outside. $pet, you are protected and safe with me.",
                    statusText = "Preset applied: ⛈️ Thunder Shield"
                )
            }
            "bedtime" -> {
                _uiState.value = _uiState.value.copy(
                    activePreset = "bedtime",
                    ambianceType = "drone",
                    ambianceVolume = 0.45f,
                    voiceVolume = 0.70f,
                    durationMinutes = 120,
                    repeatMinutes = 5,
                    delayMinutes = 0,
                    phraseText = "Time to sleep peacefully $pet. Close your eyes and dream of sunny fields.",
                    statusText = "Preset applied: 🌙 Bedtime Lullaby"
                )
            }
            "leaving" -> {
                _uiState.value = _uiState.value.copy(
                    activePreset = "leaving",
                    ambianceType = "all",
                    ambianceVolume = 0.60f,
                    voiceVolume = 0.85f,
                    durationMinutes = 30,
                    repeatMinutes = 3,
                    delayMinutes = 5,
                    phraseText = "Good boy $pet, I'll be back real soon. Stay cozy and rest.",
                    statusText = "Preset applied: 🚪 Leaving Home"
                )
            }
            "quick_nap" -> {
                _uiState.value = _uiState.value.copy(
                    activePreset = "quick_nap",
                    ambianceType = "drone",
                    ambianceVolume = 0.40f,
                    voiceVolume = 0.70f,
                    durationMinutes = 15,
                    repeatMinutes = 3,
                    delayMinutes = 0,
                    phraseText = "Relax and rest your eyes $pet. Everything is calm and peaceful.",
                    statusText = "Preset applied: 🧘 Quick Nap"
                )
            }
        }
    }

    fun saveSettings(key: String, voiceId: String, stab: Float, sim: Float) {
        val cleanKey = key.trim()
        _uiState.value = _uiState.value.copy(
            apiKey = cleanKey,
            voiceId = voiceId,
            stability = stab,
            similarity = sim,
            statusText = if (cleanKey.isNotBlank()) "ElevenLabs API Key configured ✨" else "Native TTS mode active 🔊"
        )
        viewModelScope.launch {
            prefs.saveApiKey(cleanKey)
            prefs.saveVoiceSettings(voiceId, stab, sim)
        }
    }

    fun generateVoiceTrack() {
        val state = _uiState.value
        val cleanApiKey = state.apiKey.trim().ifBlank { BuildConfig.DEFAULT_ELEVENLABS_API_KEY.trim() }
        val cleanPhrase = state.phraseText.trim()

        if (cleanPhrase.isBlank()) {
            _uiState.value = state.copy(statusText = "Please enter phrase text first ✍️")
            return
        }

        _uiState.value = state.copy(isGenerating = true, statusText = "Synthesizing voice with ElevenLabs... ✨")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (cleanApiKey.isNotBlank()) {
                    val voiceId = state.voiceId.ifBlank { "21m00Tcm4TlvDq8ikWAM" }
                    
                    // Attempt 1 with eleven_turbo_v2_5
                    var response = ElevenLabsApiClient.service.generateSpeech(
                        apiKey = cleanApiKey,
                        voiceId = voiceId,
                        request = TTSRequest(
                            text = cleanPhrase,
                            modelId = "eleven_turbo_v2_5",
                            voiceSettings = VoiceSettings(
                                stability = state.stability.coerceIn(0.3f, 1.0f),
                                similarityBoost = state.similarity.coerceIn(0.3f, 1.0f)
                            )
                        )
                    )

                    // Automatic retry after 1.5s if Rate Limited (429)
                    if (response.code() == 429) {
                        delay(1500)
                        response = ElevenLabsApiClient.service.generateSpeech(
                            apiKey = cleanApiKey,
                            voiceId = voiceId,
                            request = TTSRequest(
                                text = cleanPhrase,
                                modelId = "eleven_turbo_v2_5",
                                voiceSettings = VoiceSettings(
                                    stability = state.stability.coerceIn(0.3f, 1.0f),
                                    similarityBoost = state.similarity.coerceIn(0.3f, 1.0f)
                                )
                            )
                        )
                    }

                    if (response.isSuccessful && response.body() != null) {
                        val file = File(getApplication<Application>().cacheDir, "elevenlabs_voice.mp3")
                        FileOutputStream(file).use { output ->
                            response.body()!!.byteStream().copyTo(output)
                        }

                        val uri = Uri.fromFile(file)

                        // Play audio preview so owner hears the synthesized voice!
                        withContext(Dispatchers.Main) {
                            playPreview(file)
                            _uiState.value = _uiState.value.copy(
                                apiKey = cleanApiKey,
                                generatedAudioUri = uri,
                                isGenerating = false,
                                statusText = "ElevenLabs voice synthesized! 🐾"
                            )
                        }
                        return@launch
                    } else {
                        val errorCode = response.code()
                        val errorRaw = response.errorBody()?.string() ?: ""
                        val parsedMessage = extractErrorMessage(errorRaw, errorCode)

                        withContext(Dispatchers.Main) {
                            nativeTts?.speak(cleanPhrase, TextToSpeech.QUEUE_FLUSH, null, "tts_id")
                            _uiState.value = _uiState.value.copy(
                                isGenerating = false,
                                statusText = "$parsedMessage (Speaking Fallback TTS)"
                            )
                        }
                        return@launch
                    }
                } else {
                    // No API Key -> speak with Native TTS
                    withContext(Dispatchers.Main) {
                        nativeTts?.speak(cleanPhrase, TextToSpeech.QUEUE_FLUSH, null, "tts_id")
                        _uiState.value = _uiState.value.copy(
                            isGenerating = false,
                            statusText = "Speaking via Native TTS 🔊 (Add API Key in Settings)"
                        )
                    }
                }
            } catch (e: Exception) {
                val errorDesc = e.localizedMessage ?: "Connection error"
                withContext(Dispatchers.Main) {
                    nativeTts?.speak(cleanPhrase, TextToSpeech.QUEUE_FLUSH, null, "tts_id")
                    _uiState.value = _uiState.value.copy(
                        isGenerating = false,
                        statusText = "TTS Fallback: $errorDesc"
                    )
                }
            }
        }
    }

    private fun extractErrorMessage(json: String, code: Int): String {
        return try {
            val element = JsonParser.parseString(json)
            if (element.isJsonObject) {
                val obj = element.asJsonObject
                if (obj.has("detail")) {
                    val detail = obj.get("detail")
                    if (detail.isJsonObject) {
                        val detailObj = detail.asJsonObject
                        if (detailObj.has("message")) {
                            return detailObj.get("message").asString
                        }
                    } else if (detail.isJsonPrimitive) {
                        return detail.asString
                    }
                }
            }
            when (code) {
                401 -> "ElevenLabs 401: Invalid API Key"
                429 -> "ElevenLabs: Concurrency/Rate limit (wait 2s and retry)"
                422 -> "ElevenLabs 422: Invalid Voice Parameters"
                else -> "ElevenLabs Error $code"
            }
        } catch (_: Exception) {
            when (code) {
                401 -> "ElevenLabs 401: Invalid API Key"
                429 -> "ElevenLabs: Concurrency/Rate limit (wait 2s and retry)"
                else -> "ElevenLabs Error $code"
            }
        }
    }

    private fun playPreview(file: File) {
        try {
            previewPlayer?.stop()
            previewPlayer?.release()
            previewPlayer = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
                playPreview(file)
                _uiState.value = _uiState.value.copy(
                    isRecording = false,
                    generatedAudioUri = Uri.fromFile(file),
                    statusText = "Owner voice recorded! 🐾"
                )
            }
        }
    }

    fun startPlayback(context: Context) {
        val state = _uiState.value
        _uiState.value = state.copy(isPlaying = true, statusText = "Active: Soothing pup 🐾")
        val intent = Intent(context, PlaybackService::class.java).apply {
            action = PlaybackService.ACTION_START
            putExtra(PlaybackService.EXTRA_VOICE_URI, state.generatedAudioUri?.toString())
            putExtra(PlaybackService.EXTRA_DURATION_MIN, state.durationMinutes)
            putExtra(PlaybackService.EXTRA_REPEAT_MIN, state.repeatMinutes)
            putExtra(PlaybackService.EXTRA_AMBIANCE_TYPE, state.ambianceType)
            putExtra(PlaybackService.EXTRA_DELAY_MIN, state.delayMinutes)
        }
        context.startService(intent)
    }

    fun stopPlayback(context: Context) {
        previewPlayer?.stop()
        _uiState.value = _uiState.value.copy(isPlaying = false, statusText = "Ready to soothe 🐾")
        val intent = Intent(context, PlaybackService::class.java).apply {
            action = PlaybackService.ACTION_STOP
        }
        context.startService(intent)
    }

    override fun onCleared() {
        previewPlayer?.stop()
        previewPlayer?.release()
        previewPlayer = null
        nativeTts?.stop()
        nativeTts?.shutdown()
        super.onCleared()
    }
}
