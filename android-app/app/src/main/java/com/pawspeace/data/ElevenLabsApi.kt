package com.pawspeace.data

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming
import java.util.concurrent.TimeUnit

data class VoiceSettings(
    val stability: Float = 0.75f,
    @SerializedName("similarity_boost") val similarityBoost: Float = 0.85f,
    val style: Float = 0.15f,
    @SerializedName("use_speaker_boost") val useSpeakerBoost: Boolean = true
)

data class TTSRequest(
    val text: String,
    @SerializedName("model_id") val modelId: String = "eleven_multilingual_v2",
    @SerializedName("voice_settings") val voiceSettings: VoiceSettings = VoiceSettings()
)

data class VoicePreset(
    val id: String,
    val name: String,
    val description: String
)

val PRESET_VOICES = listOf(
    VoicePreset("21m00Tcm4TlvDq8ikWAM", "Rachel", "Warm, calm & gentle tone (Default)"),
    VoicePreset("EXAVITQu4vr4xnSDxMaL", "Bella", "Soft & soothing female voice"),
    VoicePreset("ErXwobaYiN019PkySvjV", "Antoni", "Gentle, deep reassuring male voice"),
    VoicePreset("AZnzlk1XvdvUeBnXmlld", "Domi", "Quiet & comforting tone"),
    VoicePreset("MF3mGyEYCl7XYWbV9V6O", "Elli", "Sweet & peaceful reassurance")
)

interface ElevenLabsService {
    @Streaming
    @POST("v1/text-to-speech/{voice_id}")
    suspend fun generateSpeech(
        @Header("xi-api-key") apiKey: String,
        @Path("voice_id") voiceId: String,
        @Body request: TTSRequest
    ): Response<ResponseBody>
}

object ElevenLabsApiClient {
    private const val BASE_URL = "https://api.elevenlabs.io/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val service: ElevenLabsService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ElevenLabsService::class.java)
    }
}
