# 🐾 Paws & Peace — "Home Alone" Calming Dog Playlist & Companion System

[![Live Demo](https://img.shields.io/badge/Live%20Demo-Vercel-000000?logo=vercel&logoColor=white)](https://weekend-challenge-dog-days-edition-phi.vercel.app)
[![React](https://img.shields.io/badge/React-18.3-61dafb?logo=react&logoColor=black)](https://reactjs.org/)
[![Vite](https://img.shields.io/badge/Vite-6.1-646CFF?logo=vite&logoColor=white)](https://vitejs.dev/)
[![ElevenLabs](https://img.shields.io/badge/ElevenLabs-Turbo%20v2.5-orange)](https://elevenlabs.io/)
[![Android](https://img.shields.io/badge/Android-Jetpack%20Compose-3DDC84?logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

> A modern, multi-platform sensory comfort system engineered to alleviate canine separation anxiety when pets are left home alone. Combines personalized ultra-low latency AI voice loops (powered by ElevenLabs), owner voice recording, procedural audio soundscapes (pink-noise rain, rhythmic maternal heartbeat, 432Hz ambient drone), and an intelligent interval timer.

🔗 **Live Web Application**: [https://weekend-challenge-dog-days-edition-phi.vercel.app](https://weekend-challenge-dog-days-edition-phi.vercel.app)

---

## 🌟 Key Features

### 1. 🎙️ Comforting Voice Loops (AI & Owner Studio)
- **ElevenLabs AI Voice Synthesis**: Generates lifelike, soothing voice messages using top voice models (*Rachel, Bella, Antoni, Domi, Elli*).
- **High-Throughput Turbo Engine**: Utilizes ElevenLabs' `eleven_turbo_v2_5` model for ultra-low latency (<300ms) and high concurrency limits.
- **Auto-Retry & Rate Limit Handling**: Automatic 1.5s exponential backoff retry mechanism with informative error feedback for API rate limits and character quotas.
- **Voice Customization**: Fine-tune stability and clarity/similarity boost sliders for the ultimate calming tone.
- **Owner Voice Studio**: Record personalized reassurance messages directly with microphone support and instant audio playback preview.
- **Zero-Config Fallback**: Automatic graceful fallback to the browser's Web Speech API and native Android TTS when offline or without an API key.

### 2. 🌧️ Layered Procedural Soundscapes
- **Real-Time Sound Synthesis**: Generates clean, infinite audio dynamically without relying on heavy external audio files or internet streaming.
- **Pink-Noise Rainfall**: Dampens abrupt outside noises (thunder, doorbells, street traffic).
- **Maternal Heartbeat Simulator**: Rhythmic low-frequency pulse simulating a mother dog's calming heartbeat.
- **432Hz / 528Hz Harmonic Drone**: Gentle sinusoidal resonance scientifically recognized to lower canine cortisol and heart rate.
- **Independent Audio Mixer**: Full-channel volume controls for voice, rain, heartbeat, drone, and master volume.

### 3. ⏱️ Smart Session Scheduler & Interval Loops
- **Departure Delay Timer**: Delay audio playback (e.g., 5 minutes after owner leaves) to allow the pet to settle naturally.
- **Voice Repeat Interval**: Automatically re-plays comforting voice phrases at configurable intervals (e.g., every 2–5 minutes).
- **Auto-Fade Duration**: Configurable session duration (15m, 30m, 1h, 2h, 4h, or continuous Loop ♾️) with smooth fade-out.

### 4. 🧘 Sensory Breathing Visualizer & Hero Controls
- **Paced Breathing Visualizer**: Luminous multi-ring ripple visualizer calibrated for calming visual biofeedback.
- **Dynamic Equalizer**: Live audio wave visualizer synchronized with active playback.
- **One-Tap Quick Presets**: Instant configuration for common scenarios:
  - ⛈️ *Thunder Shield* (Heavy pink rain + frequent reassurance)
  - 🌙 *Bedtime Lullaby* (432Hz harmonic drone + gentle voice)
  - 🚪 *Leaving Home* (Full audio blend + 5m departure delay)
  - 🧘 *Quick Nap* (15-minute quick restorative session)

### 5. 📱 Dual Platform Architecture
- **Web App**: Glassmorphic, dark-mode web application deployed on Vercel at [https://weekend-challenge-dog-days-edition-phi.vercel.app](https://weekend-challenge-dog-days-edition-phi.vercel.app).
- **Android App**: Native Android app built with Jetpack Compose, Material 3, AndroidX Media3 (ExoPlayer), Jetpack DataStore, and a dedicated foreground playback service for uninterrupted background lock-screen playback.

---

## 📁 Repository Structure

```
├── .env.example                # Environment variables template
├── .gitignore                  # Git ignore rules for Web and Android
├── index.html                  # Web app HTML entry point
├── package.json                # Web app dependencies & scripts
├── vercel.json                 # Vercel SPA routing configuration
├── vite.config.js              # Vite configuration
├── src/                        # React Web App Source Code
│   ├── App.jsx                 # Main layout & application state
│   ├── main.jsx                # React root mount
│   ├── index.css               # Design system tokens, glassmorphism & animations
│   ├── components/
│   │   ├── ApiKeyModal.jsx     # ElevenLabs Voice settings modal
│   │   ├── Header.jsx          # App header with pet name tag & settings trigger
│   │   ├── PhraseEditor.jsx    # Voice synthesis & reassurance builder
│   │   ├── PlayerView.jsx      # Playback controls & breathing visualizer
│   │   ├── SoundscapeMixer.jsx # Multi-track volume mixer
│   │   ├── TimerScheduler.jsx  # Duration & repeat interval sliders
│   │   └── VoiceRecorder.jsx   # Microphone recording & audio preview
│   └── services/
│       ├── audioEngine.js      # Web Audio API procedural sound synthesizer
│       └── elevenlabs.js       # ElevenLabs API client & fallback handler
└── android-app/                # Native Android Application
    ├── app/
    │   └── src/main/java/com/pawspeace/
    │       ├── MainActivity.kt
    │       ├── audio/
    │       │   ├── AudioSynthesizer.kt    # AudioTrack PCM procedural sound generator
    │       │   └── VoiceRecorderHelper.kt # Android MediaRecorder audio studio
    │       ├── data/
    │       │   ├── ElevenLabsApi.kt       # Retrofit client with Turbo v2.5 model
    │       │   └── PreferencesManager.kt  # Jetpack DataStore preferences
    │       ├── service/
    │       │   └── PlaybackService.kt     # Media3 foreground audio service
    │       ├── ui/
    │       │   ├── components/            # Compose components (Mixer, Editor, Timer, Visualizer)
    │       │   ├── screens/HomeScreen.kt  # Main responsive dashboard
    │       │   └── theme/                 # Dark luxury theme tokens & typography
    │       └── viewmodel/
    │           └── HomeViewModel.kt       # State management, auto-retry & playback preview
    ├── build.gradle.kts
    └── settings.gradle.kts
```

---

## 🌐 Live Web App & Deployment

- **Live URL**: [https://weekend-challenge-dog-days-edition-phi.vercel.app](https://weekend-challenge-dog-days-edition-phi.vercel.app)
- **Hosting Platform**: Vercel (Automated CI/CD from `main` branch)

### Local Development:
```bash
git clone https://github.com/inusha-thathsara/Weekend-Challenge-Dog-Days-Edition---Home-Alone-Playlist.git
cd Weekend-Challenge-Dog-Days-Edition---Home-Alone-Playlist
npm install
npm run dev
```

---

## 📱 Android App Setup

### Prerequisites
- Android Studio Ladybug (2024.2+) or newer
- JDK 17
- Android SDK 35 (minSdk 26 — Android 8.0 Oreo or higher)

### Automatic API Key Sharing:
The Android build system automatically reads `VITE_ELEVENLABS_API_KEY` from the root `.env` file at build time and injects it into `BuildConfig.DEFAULT_ELEVENLABS_API_KEY`. You can also configure or override your API key directly in the app's **Settings & Voice Studio** dialog.

### Command Line Build:
```bash
cd android-app
./gradlew.bat assembleDebug
```
The compiled debug APK will be generated at:
`android-app/app/build/outputs/apk/debug/app-debug.apk`

---

## 🛠️ Tech Stack & Libraries

- **Web**: React 18, Vite 6, Lucide React, Web Audio API, Web Speech API.
- **AI Voice Engine**: ElevenLabs REST API (`eleven_turbo_v2_5`), Retrofit 2, OkHttp 4, Gson.
- **Android UI & Core**: Kotlin 2.1, Jetpack Compose, Material 3, AndroidX Lifecycle / ViewModel.
- **Android Background Audio**: AndroidX Media3 (ExoPlayer), AudioTrack Real-time Synthesizer, Foreground Service Media Playback.
- **Persistence**: Jetpack DataStore Preferences.

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
