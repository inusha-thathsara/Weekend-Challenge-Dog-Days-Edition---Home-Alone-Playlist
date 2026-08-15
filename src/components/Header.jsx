import React from 'react';
import { Settings, Heart } from 'lucide-react';

export default function Header({ petName, setPetName, apiKey, onOpenSettings }) {
  return (
    <header className="header">
      <div className="header-brand">
        <div className="header-logo">🐾</div>
        <div>
          <div className="header-title">Paws & Peace</div>
          <div className="header-subtitle">Home Alone Calming Playlist & Voice Loop</div>
        </div>
      </div>

      <div className="header-actions">
        <div className="pet-badge">
          <Heart size={15} color="#f5a623" fill="rgba(245,166,35,0.2)" />
          <label>For Pup:</label>
          <input
            type="text"
            value={petName}
            onChange={(e) => setPetName(e.target.value)}
            placeholder="Pup's name"
          />
        </div>

        <button onClick={onOpenSettings} className="settings-btn">
          <Settings size={15} color="#6366f1" />
          <span>ElevenLabs Settings</span>
          <span className={`dot ${apiKey ? 'dot-green' : 'dot-amber'}`} />
        </button>
      </div>
    </header>
  );
}
