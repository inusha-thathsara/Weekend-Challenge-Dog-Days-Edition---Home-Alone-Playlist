import React, { useState } from 'react';
import { X, Sparkles, Check, Sliders } from 'lucide-react';
import { PRESET_VOICES, fetchElevenLabsSpeech } from '../services/elevenlabs';

export default function ApiKeyModal({
  isOpen,
  onClose,
  apiKey,
  voiceId,
  setVoiceId,
  stability,
  setStability,
  similarity,
  setSimilarity
}) {
  const [testing, setTesting] = useState(false);
  const [testSuccess, setTestSuccess] = useState(null);
  const [errorMsg, setErrorMsg] = useState('');

  if (!isOpen) return null;

  const handleTest = async () => {
    if (!apiKey) {
      setErrorMsg('No ElevenLabs API key configured in .env file.');
      return;
    }
    setTesting(true);
    setErrorMsg('');
    setTestSuccess(null);
    try {
      const url = await fetchElevenLabsSpeech({
        apiKey,
        voiceId,
        text: "Hello! This is a soothing voice test for your dog.",
        stability,
        similarity
      });
      new Audio(url).play();
      setTestSuccess('Voice sample generated! Playing test audio...');
    } catch (err) {
      setErrorMsg(err.message || 'Failed to connect.');
    } finally {
      setTesting(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={e => e.target === e.currentTarget && onClose()}>
      <div className="modal-card">
        <button onClick={onClose} className="modal-close"><X size={18} /></button>

        <div className="modal-title-row">
          <div className="modal-icon"><Sliders size={18} /></div>
          <div>
            <h2 style={{ fontSize: 18, fontWeight: 700, color: '#f1f5f9' }}>ElevenLabs Voice Settings</h2>
            <p style={{ fontSize: 12, color: '#64748b' }}>Configure voice presets & soothing tone</p>
          </div>
        </div>

        {/* Voice Selection */}
        <div style={{ marginBottom: 20 }}>
          <label className="section-label">Soothing Voice Preset</label>
          <div className="voice-list">
            {PRESET_VOICES.map(v => (
              <div
                key={v.id}
                className={`voice-option ${voiceId === v.id ? 'selected' : ''}`}
                onClick={() => setVoiceId(v.id)}
              >
                <div>
                  <div className="name">{v.name}</div>
                  <div className="desc">{v.description}</div>
                </div>
                {voiceId === v.id && (
                  <div className="voice-check"><Check size={13} /></div>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* Tuning */}
        <div className="tuning-grid" style={{ marginBottom: 20 }}>
          <div className="slider-group">
            <div className="slider-header">
              <span>Voice Stability</span>
              <span className="slider-value" style={{ color: '#6366f1' }}>{stability}</span>
            </div>
            <input
              type="range"
              min="0.3"
              max="1.0"
              step="0.05"
              value={stability}
              onChange={e => setStability(e.target.value)}
            />
            <span className="hint">Higher = calmer & more consistent</span>
          </div>
          <div className="slider-group">
            <div className="slider-header">
              <span>Clarity / Similarity</span>
              <span className="slider-value" style={{ color: '#6366f1' }}>{similarity}</span>
            </div>
            <input
              type="range"
              min="0.3"
              max="1.0"
              step="0.05"
              value={similarity}
              onChange={e => setSimilarity(e.target.value)}
            />
            <span className="hint">Higher = clearer articulation</span>
          </div>
        </div>

        {/* Alerts */}
        {errorMsg && <div className="alert alert-error" style={{ marginBottom: 16 }}>{errorMsg}</div>}
        {testSuccess && (
          <div className="alert alert-success" style={{ marginBottom: 16 }}>
            <Check size={14} /> {testSuccess}
          </div>
        )}

        {/* Actions */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: 16, borderTop: '1px solid rgba(255,255,255,0.07)' }}>
          <button onClick={handleTest} disabled={testing} className="btn btn-ghost">
            <Sparkles size={14} color="#f5a623" />
            {testing ? 'Testing…' : 'Test Voice'}
          </button>
          <div style={{ display: 'flex', gap: 10 }}>
            <button onClick={onClose} className="btn btn-primary">Done</button>
          </div>
        </div>
      </div>
    </div>
  );
}
