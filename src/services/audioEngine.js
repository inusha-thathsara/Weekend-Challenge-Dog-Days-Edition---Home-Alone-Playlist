/**
 * Web Audio Engine for Procedural Ambiance, Voice Track Mixing & Scheduler
 */

class AudioEngine {
  constructor() {
    this.ctx = null;
    this.masterGain = null;
    this.voiceGain = null;

    // Ambiance nodes & gain controls
    this.ambianceNodes = {};
    this.ambianceGains = {};

    // Voice track
    this.voiceAudioElement = null;
    this.voiceSourceNode = null;

    // State & Timers
    this.isPlaying = false;
    this.isPaused = false;
    this.timerInterval = null;
    this.delayTimer = null;
    this.repeatVoiceTimer = null;

    this.durationSeconds = 0; // 0 = continuous
    this.remainingSeconds = 0;
    this.repeatIntervalMinutes = 3; // default repeat voice every 3 mins

    // Callbacks
    this.onTick = null;
    this.onStatus = null;

    // Default Ambiance Volumes (0.0 to 1.0)
    this.volumes = {
      rain: 0.4,
      heartbeat: 0.5,
      waves: 0.0,
      drone: 0.3,
      voice: 0.9,
      master: 0.8
    };

    this.activeAmbianceType = 'rain'; // default primary ambiance
  }

  init() {
    if (!this.ctx) {
      const AudioCtx = window.AudioContext || window.webkitAudioContext;
      this.ctx = new AudioCtx();

      // Master Gain
      this.masterGain = this.ctx.createGain();
      this.masterGain.gain.value = this.volumes.master;
      this.masterGain.connect(this.ctx.destination);

      // Voice Gain
      this.voiceGain = this.ctx.createGain();
      this.voiceGain.gain.value = this.volumes.voice;
      this.voiceGain.connect(this.masterGain);
    }

    if (this.ctx.state === 'suspended') {
      this.ctx.resume();
    }
  }

  // ----------------------------------------------------
  // PROCEDURAL AMBIENCE SYNTHESIZERS
  // ----------------------------------------------------

  createNoiseBuffer(type = 'pink') {
    const bufferSize = this.ctx.sampleRate * 2; // 2 seconds buffer
    const buffer = this.ctx.createBuffer(1, bufferSize, this.ctx.sampleRate);
    const output = buffer.getChannelData(0);

    let b0 = 0, b1 = 0, b2 = 0, b3 = 0, b4 = 0, b5 = 0, b6 = 0;

    for (let i = 0; i < bufferSize; i++) {
      const white = Math.random() * 2 - 1;
      if (type === 'pink') {
        b0 = 0.99886 * b0 + white * 0.0555179;
        b1 = 0.99332 * b1 + white * 0.0750759;
        b2 = 0.96900 * b2 + white * 0.1538520;
        b3 = 0.86650 * b3 + white * 0.3104856;
        b4 = 0.55000 * b4 + white * 0.5329522;
        b5 = -0.7616 * b5 - white * 0.0168980;
        output[i] = b0 + b1 + b2 + b3 + b4 + b5 + b6 + white * 0.5362;
        output[i] *= 0.11; // scale volume down
        b6 = white * 0.115926;
      } else {
        output[i] = white * 0.1;
      }
    }
    return buffer;
  }

  startRainAmbiance() {
    if (this.ambianceNodes.rain) return;

    const noiseBuffer = this.createNoiseBuffer('pink');
    const noiseSource = this.ctx.createBufferSource();
    noiseSource.buffer = noiseBuffer;
    noiseSource.loop = true;

    // Filter to simulate rain hitting window/ground
    const filter = this.ctx.createBiquadFilter();
    filter.type = 'lowpass';
    filter.frequency.value = 1200;

    const gainNode = this.ctx.createGain();
    gainNode.gain.value = this.volumes.rain;

    noiseSource.connect(filter);
    filter.connect(gainNode);
    gainNode.connect(this.masterGain);

    noiseSource.start();

    this.ambianceNodes.rain = { source: noiseSource, filter, gain: gainNode };
    this.ambianceGains.rain = gainNode;
  }

  stopRainAmbiance() {
    if (this.ambianceNodes.rain) {
      try { this.ambianceNodes.rain.source.stop(); } catch(e){}
      delete this.ambianceNodes.rain;
      delete this.ambianceGains.rain;
    }
  }

  startHeartbeatAmbiance() {
    if (this.ambianceNodes.heartbeat) return;

    // Create periodic heartbeat (Lub-Dub sound at 60 BPM)
    const gainNode = this.ctx.createGain();
    gainNode.gain.value = this.volumes.heartbeat;
    gainNode.connect(this.masterGain);

    let step = 0;
    const interval = setInterval(() => {
      if (!this.isPlaying) return;

      const now = this.ctx.currentTime;
      
      // "Lub" pulse
      const osc1 = this.ctx.createOscillator();
      const oscGain1 = this.ctx.createGain();
      osc1.frequency.setValueAtTime(65, now);
      osc1.frequency.exponentialRampToValueAtTime(30, now + 0.15);

      oscGain1.gain.setValueAtTime(0.8, now);
      oscGain1.gain.exponentialRampToValueAtTime(0.001, now + 0.15);

      osc1.connect(oscGain1);
      oscGain1.connect(gainNode);
      osc1.start(now);
      osc1.stop(now + 0.16);

      // "Dub" pulse (slightly lighter, 0.2s after Lub)
      const osc2 = this.ctx.createOscillator();
      const oscGain2 = this.ctx.createGain();
      osc2.frequency.setValueAtTime(55, now + 0.2);
      osc2.frequency.exponentialRampToValueAtTime(25, now + 0.32);

      oscGain2.gain.setValueAtTime(0.5, now + 0.2);
      oscGain2.gain.exponentialRampToValueAtTime(0.001, now + 0.32);

      osc2.connect(oscGain2);
      oscGain2.connect(gainNode);
      osc2.start(now + 0.2);
      osc2.stop(now + 0.33);

    }, 1000); // 60 BPM = 1 beat per sec

    this.ambianceNodes.heartbeat = { interval, gain: gainNode };
    this.ambianceGains.heartbeat = gainNode;
  }

  stopHeartbeatAmbiance() {
    if (this.ambianceNodes.heartbeat) {
      clearInterval(this.ambianceNodes.heartbeat.interval);
      delete this.ambianceNodes.heartbeat;
      delete this.ambianceGains.heartbeat;
    }
  }

  startDroneAmbiance() {
    if (this.ambianceNodes.drone) return;

    // 432 Hz Solfeggio / Soft Harmony (432Hz + 540Hz major third)
    const osc1 = this.ctx.createOscillator();
    const osc2 = this.ctx.createOscillator();
    osc1.frequency.value = 216; // A3 harmonic
    osc2.frequency.value = 270; // C#4 harmonic

    const filter = this.ctx.createBiquadFilter();
    filter.type = 'lowpass';
    filter.frequency.value = 400;

    const gainNode = this.ctx.createGain();
    gainNode.gain.value = this.volumes.drone * 0.5;

    osc1.connect(filter);
    osc2.connect(filter);
    filter.connect(gainNode);
    gainNode.connect(this.masterGain);

    osc1.start();
    osc2.start();

    this.ambianceNodes.drone = { osc1, osc2, gain: gainNode };
    this.ambianceGains.drone = gainNode;
  }

  stopDroneAmbiance() {
    if (this.ambianceNodes.drone) {
      try {
        this.ambianceNodes.drone.osc1.stop();
        this.ambianceNodes.drone.osc2.stop();
      } catch(e){}
      delete this.ambianceNodes.drone;
      delete this.ambianceGains.drone;
    }
  }

  // ----------------------------------------------------
  // VOICE TRACK PLAYBACK
  // ----------------------------------------------------

  playVoiceAudio(audioUrl, onComplete) {
    if (!audioUrl) return;

    if (this.voiceAudioElement) {
      this.voiceAudioElement.pause();
    }

    this.voiceAudioElement = new Audio(audioUrl);
    this.voiceAudioElement.crossOrigin = 'anonymous';

    if (!this.voiceSourceNode && this.ctx) {
      this.voiceSourceNode = this.ctx.createMediaElementSource(this.voiceAudioElement);
      this.voiceSourceNode.connect(this.voiceGain);
    }

    this.voiceAudioElement.play().catch(err => {
      console.warn('Audio play auto-block:', err);
    });

    if (onComplete) {
      this.voiceAudioElement.onended = onComplete;
    }
  }

  // ----------------------------------------------------
  // SESSION CONTROLLER
  // ----------------------------------------------------

  startSession({ voiceAudioUrl, durationMinutes = 0, repeatMinutes = 3, ambianceType = 'rain', delayMinutes = 0 }) {
    this.init();
    this.stopSession(); // reset existing

    if (delayMinutes > 0) {
      this.updateStatus(`Scheduled to start in ${delayMinutes} min... ⏳`);
      this.delayTimer = setTimeout(() => {
        this.executeStart({ voiceAudioUrl, durationMinutes, repeatMinutes, ambianceType });
      }, delayMinutes * 60 * 1000);
      return;
    }

    this.executeStart({ voiceAudioUrl, durationMinutes, repeatMinutes, ambianceType });
  }

  executeStart({ voiceAudioUrl, durationMinutes, repeatMinutes, ambianceType }) {
    this.isPlaying = true;
    this.isPaused = false;
    this.activeAmbianceType = ambianceType;
    this.repeatIntervalMinutes = repeatMinutes;

    // Reset Master Gain
    this.masterGain.gain.setValueAtTime(this.volumes.master, this.ctx.currentTime);

    // Start background ambiance based on selection
    if (ambianceType === 'rain') this.startRainAmbiance();
    if (ambianceType === 'heartbeat') this.startHeartbeatAmbiance();
    if (ambianceType === 'drone') this.startDroneAmbiance();
    if (ambianceType === 'all') {
      this.startRainAmbiance();
      this.startHeartbeatAmbiance();
      this.startDroneAmbiance();
    }

    // Play initial voice track if available
    if (voiceAudioUrl) {
      this.playVoiceAudio(voiceAudioUrl);

      // Setup repetition timer for voice track
      if (repeatMinutes > 0) {
        this.repeatVoiceTimer = setInterval(() => {
          if (this.isPlaying && !this.isPaused) {
            this.playVoiceAudio(voiceAudioUrl);
          }
        }, repeatMinutes * 60 * 1000);
      }
    }

    // Setup Countdown Timer
    this.durationSeconds = durationMinutes * 60;
    this.remainingSeconds = this.durationSeconds;

    if (this.durationSeconds > 0) {
      this.timerInterval = setInterval(() => {
        if (!this.isPaused) {
          this.remainingSeconds--;

          if (this.onTick) {
            this.onTick(this.remainingSeconds);
          }

          // Smooth Fade-out during the last 15 seconds
          if (this.remainingSeconds <= 15 && this.remainingSeconds > 0) {
            const fadeFactor = this.remainingSeconds / 15;
            this.masterGain.gain.setValueAtTime(this.volumes.master * fadeFactor, this.ctx.currentTime);
          }

          if (this.remainingSeconds <= 0) {
            this.updateStatus('Session completed peacefully 🐾');
            this.stopSession();
          }
        }
      }, 1000);
    }

    this.updateStatus('Active: Comforting your pup 🐾');
  }

  pauseSession() {
    this.isPaused = true;
    if (this.ctx && this.ctx.state === 'running') {
      this.ctx.suspend();
    }
    if (this.voiceAudioElement) {
      this.voiceAudioElement.pause();
    }
    this.updateStatus('Session Paused ⏸️');
  }

  resumeSession() {
    this.isPaused = false;
    if (this.ctx && this.ctx.state === 'suspended') {
      this.ctx.resume();
    }
    if (this.voiceAudioElement) {
      this.voiceAudioElement.play();
    }
    this.updateStatus('Active: Comforting your pup 🐾');
  }

  stopSession() {
    this.isPlaying = false;
    this.isPaused = false;

    if (this.delayTimer) clearTimeout(this.delayTimer);
    if (this.timerInterval) clearInterval(this.timerInterval);
    if (this.repeatVoiceTimer) clearInterval(this.repeatVoiceTimer);

    this.stopRainAmbiance();
    this.stopHeartbeatAmbiance();
    this.stopDroneAmbiance();

    if (this.voiceAudioElement) {
      this.voiceAudioElement.pause();
      this.voiceAudioElement = null;
    }

    if (this.onTick) this.onTick(0);
    this.updateStatus('Ready to soothe 🐾');
  }

  setVolume(type, val) {
    this.volumes[type] = parseFloat(val);
    if (type === 'master' && this.masterGain) {
      this.masterGain.gain.setValueAtTime(this.volumes.master, this.ctx.currentTime);
    } else if (type === 'voice' && this.voiceGain) {
      this.voiceGain.gain.setValueAtTime(this.volumes.voice, this.ctx.currentTime);
    } else if (this.ambianceGains[type]) {
      this.ambianceGains[type].gain.setValueAtTime(this.volumes[type], this.ctx.currentTime);
    }
  }

  updateStatus(text) {
    if (this.onStatus) {
      this.onStatus(text);
    }
  }
}

export const audioEngine = new AudioEngine();
