import React from 'react';
import { CloudRain, Heart, Music, Waves, SlidersHorizontal, Volume2 } from 'lucide-react';

const AMBIANCE_PRESETS = [
  { id: 'rain', name: 'Gentle Rain', icon: CloudRain, color: '#22d3ee' },
  { id: 'heartbeat', name: 'Mother Heartbeat', icon: Heart, color: '#fb7185' },
  { id: 'drone', name: '432Hz Calm Drone', icon: Music, color: '#6366f1' },
  { id: 'all', name: 'Full Soundscape', icon: Waves, color: '#f5a623' }
];

export default function SoundscapeMixer({ ambianceType, setAmbianceType, volumes, onVolumeChange }) {
  return (
    <div className="card">
      <div className="card-title">
        <SlidersHorizontal size={18} color="#6366f1" />
        Background Ambiance & Soundscape
        <span className="badge">Web Audio Engine</span>
      </div>

      {/* Sound Selection Grid */}
      <div className="sound-grid">
        {AMBIANCE_PRESETS.map(p => {
          const Icon = p.icon;
          return (
            <div
              key={p.id}
              className={`sound-card ${ambianceType === p.id ? 'selected' : ''}`}
              onClick={() => setAmbianceType(p.id)}
            >
              <Icon size={20} color={p.color} className="sound-icon" />
              <span className="sound-label">{p.name}</span>
            </div>
          );
        })}
      </div>

      {/* Volume Sliders */}
      <div className="sliders-row">
        <div className="slider-group">
          <div className="slider-header">
            <span style={{ display: 'flex', alignItems: 'center', gap: 5 }}>
              <Volume2 size={13} color="#f5a623" /> Voice
            </span>
            <span className="slider-value" style={{ color: '#f5a623' }}>{Math.round(volumes.voice * 100)}%</span>
          </div>
          <input type="range" min="0" max="1" step="0.05" value={volumes.voice}
            onChange={e => onVolumeChange('voice', e.target.value)} />
        </div>

        <div className="slider-group">
          <div className="slider-header">
            <span style={{ display: 'flex', alignItems: 'center', gap: 5 }}>
              <Volume2 size={13} color="#22d3ee" /> Ambiance
            </span>
            <span className="slider-value" style={{ color: '#22d3ee' }}>{Math.round(volumes.rain * 100)}%</span>
          </div>
          <input type="range" min="0" max="1" step="0.05" value={volumes.rain}
            onChange={e => {
              onVolumeChange('rain', e.target.value);
              onVolumeChange('heartbeat', e.target.value);
              onVolumeChange('drone', e.target.value);
            }} />
        </div>

        <div className="slider-group">
          <div className="slider-header">
            <span style={{ display: 'flex', alignItems: 'center', gap: 5 }}>
              <Volume2 size={13} color="#6366f1" /> Master
            </span>
            <span className="slider-value" style={{ color: '#6366f1' }}>{Math.round(volumes.master * 100)}%</span>
          </div>
          <input type="range" min="0" max="1" step="0.05" value={volumes.master}
            onChange={e => onVolumeChange('master', e.target.value)} />
        </div>
      </div>
    </div>
  );
}
