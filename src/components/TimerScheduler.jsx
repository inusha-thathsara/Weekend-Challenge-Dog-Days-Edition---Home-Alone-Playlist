import React from 'react';
import { Timer, Repeat, Clock, Zap } from 'lucide-react';

const DURATION_OPTIONS = [
  { value: 15, label: '15 Mins' },
  { value: 30, label: '30 Mins' },
  { value: 60, label: '1 Hour' },
  { value: 120, label: '2 Hours' },
  { value: 0, label: 'Continuous' }
];

export default function TimerScheduler({
  durationMinutes, setDurationMinutes,
  repeatMinutes, setRepeatMinutes,
  delayMinutes, setDelayMinutes
}) {
  return (
    <div className="card">
      <div className="card-title">
        <Timer size={18} color="#34d399" />
        Timer & Playback Scheduler
        <span className="badge" style={{ color: '#34d399', borderColor: 'rgba(52,211,153,0.2)', background: 'rgba(52,211,153,0.06)' }}>
          Auto Fade-out
        </span>
      </div>

      {/* Duration grid */}
      <div className="section-label">
        <Clock size={13} color="#f5a623" /> Session Duration
      </div>
      <div className="duration-grid">
        {DURATION_OPTIONS.map(opt => (
          <button
            key={opt.value}
            className={`duration-btn ${durationMinutes === opt.value ? 'selected' : ''}`}
            onClick={() => setDurationMinutes(opt.value)}
          >
            {opt.label}
          </button>
        ))}
      </div>

      {/* Repeat & delay row */}
      <div className="scheduler-row">
        <div className="slider-group">
          <div className="slider-header">
            <span style={{ display: 'flex', alignItems: 'center', gap: 5 }}>
              <Repeat size={13} color="#6366f1" /> Repeat Voice Every
            </span>
            <span className="slider-value" style={{ color: '#6366f1' }}>{repeatMinutes} min</span>
          </div>
          <input type="range" min="1" max="10" step="1" value={repeatMinutes}
            onChange={e => setRepeatMinutes(parseInt(e.target.value))} />
          <span className="hint">Voice clip repeats over continuous ambient background.</span>
        </div>

        <div className="slider-group">
          <div className="slider-header">
            <span style={{ display: 'flex', alignItems: 'center', gap: 5 }}>
              <Zap size={13} color="#f5a623" /> Start Delay
            </span>
            <span className="slider-value" style={{ color: '#f5a623' }}>
              {delayMinutes === 0 ? 'Immediate' : `${delayMinutes} min`}
            </span>
          </div>
          <input type="range" min="0" max="30" step="5" value={delayMinutes}
            onChange={e => setDelayMinutes(parseInt(e.target.value))} />
          <span className="hint">Delay start so playlist begins after you leave home.</span>
        </div>
      </div>
    </div>
  );
}
