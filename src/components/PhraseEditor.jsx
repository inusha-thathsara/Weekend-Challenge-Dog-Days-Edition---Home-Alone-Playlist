import React, { useState } from 'react';
import { Sparkles, AudioWaveform, RefreshCw } from 'lucide-react';
import VoiceRecorder from './VoiceRecorder';
import { fetchElevenLabsSpeech, speakWebSpeechFallback } from '../services/elevenlabs';

const DEFAULT_PRESETS = [
  "Good boy, I'll be back real soon. You are safe.",
  "Rest now buddy, everything is calm and okay.",
  "I love you so much. Sleep well while I'm away.",
  "You're a brave dog. Just lie down and relax.",
  "Mommy and Daddy love you. Be a good pup."
];

export default function PhraseEditor({
  petName, apiKey, voiceId, stability, similarity,
  voiceMode, setVoiceMode,
  generatedAudioUrl, setGeneratedAudioUrl,
  recordedAudioUrl, setRecordedAudioUrl,
  isGenerating, setIsGenerating
}) {
  const [phrasesText, setPhrasesText] = useState(
    `Good boy ${petName || 'Buddy'}, I'll be back real soon. You are safe and loved. Rest now.`
  );

  const handleAddPreset = (text) => {
    const formatted = text.replace('Buddy', petName || 'Buddy');
    setPhrasesText(prev => prev ? `${prev} ${formatted}` : formatted);
  };

  const handleGenerate = async () => {
    if (!phrasesText.trim()) return;
    setIsGenerating(true);
    try {
      if (apiKey) {
        const url = await fetchElevenLabsSpeech({ apiKey, voiceId, text: phrasesText, stability, similarity });
        setGeneratedAudioUrl(url);
      } else {
        speakWebSpeechFallback(phrasesText);
        setGeneratedAudioUrl(null);
      }
    } catch (err) {
      alert(`Synthesis Error: ${err.message}. Falling back to Web Speech.`);
      speakWebSpeechFallback(phrasesText);
    } finally {
      setIsGenerating(false);
    }
  };

  return (
    <div className="card">
      {/* Title row with tab selector */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 16 }}>
        <div className="card-title" style={{ marginBottom: 0 }}>
          <Sparkles size={18} className="icon" />
          Comforting Voice Message
        </div>

        <div className="tab-group">
          <button
            className={`tab-item ${voiceMode === 'elevenlabs' ? 'active' : ''}`}
            onClick={() => setVoiceMode('elevenlabs')}
          >
            AI Voice
          </button>
          <button
            className={`tab-item ${voiceMode === 'recorded' ? 'active-amber' : ''}`}
            onClick={() => setVoiceMode('recorded')}
          >
            Record
          </button>
        </div>
      </div>

      {voiceMode === 'elevenlabs' ? (
        <>
          {/* Preset chips */}
          <div style={{ marginBottom: 12 }}>
            <span className="hint" style={{ marginRight: 8 }}>Presets:</span>
            <div className="preset-chips" style={{ display: 'inline-flex', flexWrap: 'wrap', gap: 6 }}>
              {DEFAULT_PRESETS.map((p, i) => (
                <button key={i} className="chip" onClick={() => handleAddPreset(p)}>
                  + "{p.substring(0, 28)}…"
                </button>
              ))}
            </div>
          </div>

          {/* Textarea */}
          <textarea
            rows={3}
            value={phrasesText}
            onChange={e => setPhrasesText(e.target.value)}
            placeholder="Type comforting phrases for your pup..."
            className="input-field"
          />

          {/* Bottom action row */}
          <div className="helper-row">
            <span className="helper-text">
              {apiKey ? '✨ Using ElevenLabs API' : '🔊 No key: Browser Web Speech fallback'}
            </span>

            <button onClick={handleGenerate} disabled={isGenerating || !phrasesText.trim()} className="btn btn-glow" style={{ padding: '10px 24px', fontSize: 13 }}>
              {isGenerating ? (
                <><RefreshCw size={15} style={{ animation: 'spin 1s linear infinite' }} /> Synthesizing…</>
              ) : (
                <><AudioWaveform size={15} /> Generate Soothing Track</>
              )}
            </button>
          </div>
        </>
      ) : (
        <VoiceRecorder
          onVoiceRecorded={url => setRecordedAudioUrl(url)}
          recordedAudioUrl={recordedAudioUrl}
        />
      )}
    </div>
  );
}
