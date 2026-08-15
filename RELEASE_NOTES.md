# 🐾 Paws & Peace Mobile — Release Notes v1.0.0

**Release Tag**: `v1.0.0-release`  
**Build Target**: Android 8.0 (API 26) – Android 15 (API 35)  
**APK Output**: `android-app/app/build/outputs/apk/debug/app-debug.apk`  

---

## 🌟 Highlights & New Features

### 🎙️ 1. Ultra-Low Latency ElevenLabs Turbo Voice Engine
- **Upgraded to `eleven_turbo_v2_5`**: Generates lifelike, gentle reassurance voice messages in under 300ms with higher concurrency limits and lower character overhead.
- **Top Soothing Voice Presets**: Curated models specifically tuned for pet calming (*Rachel, Bella, Antoni, Domi, Elli*).
- **Auto-Retry & Burst Handling**: Automatic 1.5s exponential backoff retry mechanism for API rate limits, with accurate error diagnostics directly from ElevenLabs.
- **Live Audio Preview**: Instant `MediaPlayer` preview engine plays synthesized voice tracks as soon as generation completes.

### 🌧️ 2. Real-Time Procedural Sound Generator
- **Offline AudioTrack Engine**: Synthesizes continuous audio mathematically on-device without needing internet streaming or large audio files:
  - **Pink-Noise Rainfall**: Muffles jarring exterior sounds (thunder, fireworks, car horns).
  - **Maternal Heartbeat**: Rhythmic 60–70 BPM low-frequency pulse simulating a mother dog's calming presence.
  - **432Hz Harmonic Drone**: Gentle resonant sine tones proven to lower canine heart rates and anxiety.
- **Individual Track Mixer**: Independent channel sliders for Voice, Rain, Heartbeat, and Drone.

### 📱 3. Complete Mobile UI Redesign (Jetpack Compose & Material 3)
- **Full-Width Segmented Tab Switcher**: Replaced cramped right-side buttons with a responsive 50/50 segmented tab bar (`✨ AI Voice Synthesis` vs `🎙️ Owner Voice Studio`).
- **Balanced Breathing Visualizer Hero**: 200dp sensory orb with multi-ring ripple wave animations, golden pet avatar ring, and dynamic equalizers.
- **Fixed Layout & Text Clipping**:
  - Repositioned `44.1 kHz Hi-Fi` and `Auto Fade-out` badges to eliminate vertical wrapping.
  - Added weighted label constraints to prevent slider text clipping (`"Immediate"`).
- **Polished Hero Play Button**: Floating gradient pill button with amber/rose highlights and smooth transitions.
- **One-Tap Quick Presets**: Pre-configured audio mixes for *⛈️ Thunder Shield*, *🌙 Bedtime Lullaby*, *🚪 Leaving Home*, and *🧘 Quick Nap*.

### 🔒 4. Lock-Screen Background Playback
- **AndroidX Media3 Foreground Service**: Uninterrupted audio playback that keeps running when the screen is locked or the app is minimized.
- **Lock Screen & Notification Controls**: Interactive notification with active session status and one-tap stop action.

### ⚙️ 5. Zero-Config Settings & Build Integration
- **Automated `.env` Key Injection**: Gradle automatically binds `VITE_ELEVENLABS_API_KEY` from the project `.env` into `BuildConfig` at compile time.
- **Reactive DataStore Settings**: Keyed state management in the Settings Dialog for API keys, voice selection, stability/similarity sliders, and pet profiles.

---

## 🛠️ Technical Specifications
| Specification | Value |
|---|---|
| **Version Name** | `1.0.0` |
| **Version Code** | `1` |
| **Min SDK** | `API 26 (Android 8.0 Oreo)` |
| **Target SDK** | `API 35 (Android 15)` |
| **UI Framework** | Jetpack Compose (BOM 2024.12.01) + Material 3 |
| **Kotlin Version** | `2.1.0` |
| **Audio Architecture** | AndroidX Media3 1.5.1 + AudioTrack PCM Synthesizer |
| **Networking** | Retrofit 2.11.0 + OkHttp 4.12.0 |
| **Persistence** | AndroidX Preferences DataStore 1.1.2 |

---

## 📥 Installation Instructions
1. Transfer `app-debug.apk` to your Android device via USB, Google Drive, or `adb install`:
   ```bash
   adb install -r android-app/app/build/outputs/apk/debug/app-debug.apk
   ```
2. Open **Paws & Peace** on your phone.
3. Tap **Start Calming Session** or customize your voice message in the Voice Studio.
