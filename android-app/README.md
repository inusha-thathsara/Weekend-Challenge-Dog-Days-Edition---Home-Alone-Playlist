# Paws & Peace — Android App 🐾📱

Native Android application for **Paws & Peace** ("Home Alone" Calming Dog Playlist).

## 🌟 Key Features
- **Jetpack Compose UI**: Twilight dark mode design with animated breathing rhythm circle and dynamic audio waveform equalizer.
- **Media3 Background Audio Playback**: Android Foreground Service (`PlaybackService`) with persistent notifications and lock-screen controls so the soothing playlist continues uninterrupted when your phone screen turns off.
- **ElevenLabs AI Voice Synthesis**: Converts comforting phrases into lifelike soothing voice tracks with preset voices (*Rachel, Bella, Antoni, Domi, Elli*).
- **Native TTS & Voice Recording**: Microphone recording via `VoiceRecorderHelper` and zero-config fallback to Android's built-in `TextToSpeech` engine.
- **Procedural Soundscape Generator**: Generates continuous pink-noise rain and 432Hz calming drone frequencies via `AudioTrack`.
- **Jetpack DataStore**: Securely stores API keys, custom phrases, and volume preferences.

## 🛠️ How to Open & Build

### In Android Studio:
1. Open Android Studio.
2. Select **Open**, and browse to this directory:
   `e:\Documents\Projects\Weekend Challenge Dog Days Edition\Home Alone Playlist\android-app`
3. Let Gradle sync and press **Run 'app'** (`Shift + F10`) on your connected device or emulator.

### Via Command Line:
```bash
cd android-app
./gradlew assembleDebug
```
