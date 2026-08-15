package com.pawspeace.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "paws_peace_prefs")

class PreferencesManager(private val context: Context) {

    companion object {
        val KEY_API_KEY = stringPreferencesKey("elevenlabs_api_key")
        val KEY_PET_NAME = stringPreferencesKey("pet_name")
        val KEY_VOICE_ID = stringPreferencesKey("selected_voice_id")
        val KEY_STABILITY = floatPreferencesKey("voice_stability")
        val KEY_SIMILARITY = floatPreferencesKey("voice_similarity")
        val KEY_VOICE_VOL = floatPreferencesKey("volume_voice")
        val KEY_AMBIANCE_VOL = floatPreferencesKey("volume_ambiance")
        val KEY_MASTER_VOL = floatPreferencesKey("volume_master")
    }

    val apiKeyFlow: Flow<String> = context.dataStore.data.map { it[KEY_API_KEY] ?: "" }
    val petNameFlow: Flow<String> = context.dataStore.data.map { it[KEY_PET_NAME] ?: "Buddy" }
    val voiceIdFlow: Flow<String> = context.dataStore.data.map { it[KEY_VOICE_ID] ?: "21m00Tcm4TlvDq8ikWAM" }
    val stabilityFlow: Flow<Float> = context.dataStore.data.map { it[KEY_STABILITY] ?: 0.75f }
    val similarityFlow: Flow<Float> = context.dataStore.data.map { it[KEY_SIMILARITY] ?: 0.85f }

    suspend fun saveApiKey(apiKey: String) {
        context.dataStore.edit { it[KEY_API_KEY] = apiKey }
    }

    suspend fun savePetName(name: String) {
        context.dataStore.edit { it[KEY_PET_NAME] = name }
    }

    suspend fun saveVoiceSettings(voiceId: String, stability: Float, similarity: Float) {
        context.dataStore.edit {
            it[KEY_VOICE_ID] = voiceId
            it[KEY_STABILITY] = stability
            it[KEY_SIMILARITY] = similarity
        }
    }
}
