import React from 'react';
import { Play, Pause, Square, Radio } from 'lucide-react';

const BAR_COLORS = ['#f5a623', '#6366f1', '#34d399', '#fb7185', '#22d3ee', '#a78bfa', '#fbbf24'];

export default function PlayerView({
  petName, isPlaying, isPaused, remainingSeconds, statusText,
  onStartSession, onPauseSession, onResumeSession, onStopSession
}) {
  const formatTime = (secs) => {
    if (secs <= 0) return '∞';
    const m = Math.floor(secs / 60);
    const s = secs % 60;
    return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`;
  };

  const active = isPlaying && !isPaused;

  return (
    <div className="player-panel">
      {/* Status pill */}
      <div className="status-pill">
        <Radio size={13} color={active ? '#34d399' : '#64748b'}
          style={active ? { animation: 'pulse 1.5s ease-in-out infinite' } : {}} />
        <span>{statusText || 'Ready to soothe 🐾'}</span>
      </div>

      {/* Breathing ring */}
      <div className={`breathing-ring ${active ? 'active' : ''}`}>
        <span className="emoji">🐶</span>
        <span className="pet-label">{petName || 'Pup'}</span>
        <span className="sub-label">{active ? 'Breathing Rhythm' : 'Comfort Zone'}</span>

        {active && (
          <div className="eq-bars">
            {BAR_COLORS.map((color, i) => (
              <div key={i} className="eq-bar"
                style={{ background: color, animationDelay: `${i * 0.15}s` }} />
            ))}
          </div>
        )}
      </div>

      {/* Timer */}
      <div className="timer-display">
        <div className="time">
          {remainingSeconds > 0 ? formatTime(remainingSeconds) : (
            <span>Continuous Loop <span style={{ fontSize: 28 }}>♾️</span></span>
          )}
        </div>
        <div className="time-label">
          {isPlaying ? 'Remaining Playback Time' : 'Set your duration & press Play'}
        </div>
      </div>

      {/* Controls */}
      <div className="player-actions">
        {!isPlaying ? (
          <button onClick={onStartSession} className="btn btn-glow">
            <Play size={18} fill="#0a0e17" color="#0a0e17" />
            Start Soothing Playlist
          </button>
        ) : (
          <>
            {isPaused ? (
              <button onClick={onResumeSession} className="btn btn-primary" style={{ padding: '12px 28px' }}>
                <Play size={16} fill="#fff" /> Resume
              </button>
            ) : (
              <button onClick={onPauseSession} className="btn btn-ghost" style={{ padding: '12px 28px' }}>
                <Pause size={16} /> Pause
              </button>
            )}
            <button onClick={onStopSession} className="btn btn-danger" style={{ padding: '12px 28px' }}>
              <Square size={15} fill="#fb7185" /> Stop
            </button>
          </>
        )}
      </div>
    </div>
  );
}
