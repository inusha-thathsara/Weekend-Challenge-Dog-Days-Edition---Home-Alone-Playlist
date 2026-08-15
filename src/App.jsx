import React, { useState, useEffect } from 'react';
import Header from './components/Header';
import ApiKeyModal from './components/ApiKeyModal';
import PhraseEditor from './components/PhraseEditor';
import SoundscapeMixer from './components/SoundscapeMixer';
import TimerScheduler from './components/TimerScheduler';
import PlayerView from './components/PlayerView';
import { audioEngine } from './services/audioEngine';
import { speakWebSpeechFallback } from './services/elevenlabs';

export default function App() {
  const [petName, setPetName] = useState('Buddy');
  const [apiKey, setApiKey] = useState(() => {
    return localStorage.getItem('elevenlabs_key') || import.meta.env.VITE_ELEVENLABS_API_KEY || '';
  });
  const [voiceId, setVoiceId] = useState('21m00Tcm4TlvDq8ikWAM');
  const [stability, setStability] = useState(0.75);
  const [similarity, setSimilarity] = useState(0.85);
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);

  const [voiceMode, setVoiceMode] = useState('elevenlabs');
  const [generatedAudioUrl, setGeneratedAudioUrl] = useState(null);
  const [recordedAudioUrl, setRecordedAudioUrl] = useState(null);
  const [isGenerating, setIsGenerating] = useState(false);

  const [ambianceType, setAmbianceType] = useState('rain');
  const [volumes, setVolumes] = useState({ voice: 0.9, rain: 0.4, heartbeat: 0.5, drone: 0.3, master: 0.8 });

  const [durationMinutes, setDurationMinutes] = useState(30);
  const [repeatMinutes, setRepeatMinutes] = useState(3);
  const [delayMinutes, setDelayMinutes] = useState(0);

  const [isPlaying, setIsPlaying] = useState(false);
  const [isPaused, setIsPaused] = useState(false);
  const [remainingSeconds, setRemainingSeconds] = useState(0);
  const [statusText, setStatusText] = useState('Ready to soothe 🐾');

  useEffect(() => {
    audioEngine.onTick = (secs) => setRemainingSeconds(secs);
    audioEngine.onStatus = (msg) => setStatusText(msg);
  }, []);

  const handleVolumeChange = (type, val) => {
    setVolumes(prev => ({ ...prev, [type]: parseFloat(val) }));
    audioEngine.setVolume(type, val);
  };

  const handleStart = () => {
    let url = voiceMode === 'elevenlabs' ? generatedAudioUrl : recordedAudioUrl;
    if (voiceMode === 'elevenlabs' && !url && !apiKey) {
      speakWebSpeechFallback(`Good boy ${petName}, everything is calm. I will be back soon.`);
    }
    setIsPlaying(true); setIsPaused(false);
    audioEngine.startSession({ voiceAudioUrl: url, durationMinutes, repeatMinutes, ambianceType, delayMinutes });
  };
  const handlePause = () => { setIsPaused(true); audioEngine.pauseSession(); };
  const handleResume = () => { setIsPaused(false); audioEngine.resumeSession(); };
  const handleStop = () => { setIsPlaying(false); setIsPaused(false); audioEngine.stopSession(); };

  return (
    <div className="app-container">
      <Header petName={petName} setPetName={setPetName} apiKey={apiKey} onOpenSettings={() => setIsSettingsOpen(true)} />

      <div className="main-grid">
        <div className="left-col">
          <PhraseEditor
            petName={petName} apiKey={apiKey} voiceId={voiceId}
            stability={stability} similarity={similarity}
            voiceMode={voiceMode} setVoiceMode={setVoiceMode}
            generatedAudioUrl={generatedAudioUrl} setGeneratedAudioUrl={setGeneratedAudioUrl}
            recordedAudioUrl={recordedAudioUrl} setRecordedAudioUrl={setRecordedAudioUrl}
            isGenerating={isGenerating} setIsGenerating={setIsGenerating}
          />
          <SoundscapeMixer
            ambianceType={ambianceType} setAmbianceType={setAmbianceType}
            volumes={volumes} onVolumeChange={handleVolumeChange}
          />
          <TimerScheduler
            durationMinutes={durationMinutes} setDurationMinutes={setDurationMinutes}
            repeatMinutes={repeatMinutes} setRepeatMinutes={setRepeatMinutes}
            delayMinutes={delayMinutes} setDelayMinutes={setDelayMinutes}
          />
        </div>

        <div className="right-col">
          <PlayerView
            petName={petName} isPlaying={isPlaying} isPaused={isPaused}
            remainingSeconds={remainingSeconds} statusText={statusText}
            onStartSession={handleStart} onPauseSession={handlePause}
            onResumeSession={handleResume} onStopSession={handleStop}
          />
        </div>
      </div>

      <footer className="footer">
        <p>🐾 <strong>Paws & Peace</strong> — Keeping anxious pups relaxed with gentle voices & soothing soundscapes.</p>
      </footer>

      <ApiKeyModal
        isOpen={isSettingsOpen} onClose={() => setIsSettingsOpen(false)}
        apiKey={apiKey} setApiKey={setApiKey}
        voiceId={voiceId} setVoiceId={setVoiceId}
        stability={stability} setStability={setStability}
        similarity={similarity} setSimilarity={setSimilarity}
      />
    </div>
  );
}
