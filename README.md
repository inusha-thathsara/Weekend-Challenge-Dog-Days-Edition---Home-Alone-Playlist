# 🐾 Paws & Peace — "Home Alone" Calming Dog Playlist & Companion System

[![React](https://img.shields.io/badge/React-18.3-61dafb?logo=react&logoColor=black)](https://reactjs.org/)
[![Vite](https://img.shields.io/badge/Vite-6.1-646CFF?logo=vite&logoColor=white)](https://vitejs.dev/)
[![ElevenLabs](https://img.shields.io/badge/ElevenLabs-AI%20Voice-orange)](https://elevenlabs.io/)
[![Android](https://img.shields.io/badge/Android-Jetpack%20Compose-3DDC84?logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

> A modern, multi-platform sensory comfort system designed to relieve canine separation anxiety when pets are left home alone. Combines personalized AI voice loops, owner voice recording, procedural soundscapes (pink-noise rain, rhythmic heartbeat, 432Hz ambient drone), and an intelligent interval timer.

---

## 🌟 Key Features

### 1. 🎙️ Comforting Voice Loops (AI & Real Voice)
- **ElevenLabs AI Voice Synthesis**: Generates lifelike, gentle voice messages using top soothing voice models (*Rachel, Adam, Bella, Antoni, Domi, Elli*).
- **Voice Customization**: Fine-tune stability and clarity sliders for the ultimate calming tone.
- **In-Browser Voice Recorder**: Allows pet parents to record their own voice directly with microphone support.
- **Zero-Config Fallback**: Built-in fallback to the browser's Web Speech API and native Android TTS when offline or without an API key.

### 2. 🌧️ Layered Procedural Soundscapes
- **Real-Time Web Audio Engine**: Generates dynamic audio without relying on heavy external audio files.
- **Pink-Noise Rainfall**: Blocks sudden outside noises (thunder, doorbells, street noise).
- **Maternal Heartbeat Simulator**: Rhythmic low-frequency pulse simulating a mother dog's calming heartbeat.
- **432Hz / 528Hz Calming Drone**: Gentle harmonic resonance scientifically known to lower canine heart rates.
- **Individual Mixer Channel Sliders**: Independent volume controls for voice, rain, heartbeat, drone, and master volume.

### 3. ⏱️ Smart Session Scheduler & Interval Loops
- **Departure Delay Timer**: Start calming audio after a set delay (e.g., 5 minutes after leaving).
- **Voice Repeat Interval**: Automatically re-plays comforting voice phrases every $X$ minutes (e.g., every 3 minutes) to reassure the pet.
- **Auto-Fade Duration**: Configurable overall session duration (15m, 30m, 1h, 2h, 4h) with smooth fade-out.

### 4. 🧘 Visual Rhythms & Player View
- **Breathing Circle Visualizer**: Expanding and contracting visual guide paced for relaxation.
- **Live Waveform & Countdown**: Real-time status indicators and remaining session duration clock.

### 5. 📱 Dual Platform Support
- **Web App**: Responsive, dark-mode glassmorphic interface built with React + Vite + TailwindCSS.
- **Android App**: Native Android app built with Jetpack Compose, Material 3, and an AndroidX Media3 foreground playback service for uninterrupted lock-screen audio playback.

---

## 📁 Repository Structure

```
├── .env.example                # Example environment variables template
├── .gitignore                  # Git ignore rules for Web and Android
├── index.html                  # HTML entry point with modern typography
├── package.json                # Web app dependencies & npm scripts
├── vite.config.js              # Vite configuration
├── src/                        # Web App Source Code
│   ├── App.jsx                 # Main application state & layout
│   ├── main.jsx                # React root mount
│   ├── index.css               # Design system, glassmorphism & animations
│   ├── components/
│   │   ├── ApiKeyModal.jsx     # ElevenLabs Voice settings & preset selector
│   │   ├── Header.jsx          # Header with pet name tag & settings trigger
│   │   ├── PhraseEditor.jsx    # Voice synthesis & phrase builder
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
    │       ├── audio/          # Audio synthesizer & audio engine
    │       ├── service/        # Media3 foreground playback service
    │       ├── ui/             # Jetpack Compose UI components & theme
    │       └── viewmodel/      # HomeViewModel & DataStore preferences
    ├── build.gradle.kts
    └── settings.gradle.kts
```

---

## 🚀 Web App Quick Start

### Prerequisites
- [Node.js](https://nodejs.org/) (version 18+ recommended)
- (Optional) [ElevenLabs API Key](https://elevenlabs.io/)

### 1. Clone & Install Dependencies
```bash
git clone https://github.com/inusha-thathsara/Weekend-Challenge-Dog-Days-Edition---Home-Alone-Playlist.git
cd Weekend-Challenge-Dog-Days-Edition---Home-Alone-Playlist
npm install
```

### 2. Configure Environment Variables
Create a `.env` file in the project root (or copy `.env.example`):
```bash
cp .env.example .env
```
Add your ElevenLabs API Key:
```env
VITE_ELEVENLABS_API_KEY=your_elevenlabs_api_key_here
```
*(Note: If no API key is provided, the application will gracefully fall back to the browser's built-in Web Speech synthesis.)*

### 3. Run Development Server
```bash
npm run dev
```
Open [http://localhost:3000](http://localhost:3000) in your browser.

### 4. Build for Production
```bash
npm run build
```
The optimized production bundle will be generated in the `dist/` directory.

---

## 🌐 Deploying to Vercel

You can deploy the web app to **Vercel** with zero configuration:

1. Push this repository to your GitHub account.
2. Go to [Vercel Dashboard](https://vercel.com/dashboard) and click **"Add New..."** → **"Project"**.
3. Import this repository.
4. Set the Environment Variable:
   - **Name**: `VITE_ELEVENLABS_API_KEY`
   - **Value**: *Your ElevenLabs API Key*
5. Click **Deploy**.

---

## 📱 Android App Setup

### In Android Studio:
1. Open Android Studio.
2. Select **Open** and choose the `android-app` folder inside this repository.
3. Allow Gradle to sync dependencies.
4. Run on an emulator or physical device running Android 8.0 (API 26) or higher.

### Command Line Build:
```bash
cd android-app
./gradlew assembleDebug
```
The debug APK will be located at `android-app/app/build/outputs/apk/debug/app-debug.apk`.

---

## 🛠️ Built With

- **Web**: React 18, Vite 6, TailwindCSS 4, Lucide React, Web Audio API, Web Speech API
- **AI Audio**: ElevenLabs Text-to-Speech API
- **Mobile**: Kotlin 2.0, Android Jetpack Compose, AndroidX Media3, AudioTrack Synthesizer, Jetpack DataStore

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
