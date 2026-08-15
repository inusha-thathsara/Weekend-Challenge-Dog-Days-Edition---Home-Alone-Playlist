/**
 * ElevenLabs Speech Synthesis Service & Fallbacks
 */

export const PRESET_VOICES = [
  { id: '21m00Tcm4TlvDq8ikWAM', name: 'Rachel', description: 'Warm, calm & gentle tone (Popular)' },
  { id: 'EXAVITQu4vr4xnSDxMaL', name: 'Bella', description: 'Soft & soothing female voice' },
  { id: 'ErXwobaYiN019PkySvjV', name: 'Antoni', description: 'Gentle, deep reassuring male voice' },
  { id: 'AZnzlk1XvdvUeBnXmlld', name: 'Domi', description: 'Quiet & comforting' },
  { id: 'MF3mGyEYCl7XYWbV9V6O', name: 'Elli', description: 'Sweet & peaceful voice' }
];

export async function fetchElevenLabsSpeech({ apiKey, voiceId, text, stability = 0.75, similarity = 0.85 }) {
  if (!apiKey) {
    throw new Error('No ElevenLabs API Key provided.');
  }

  const selectedVoice = voiceId || '21m00Tcm4TlvDq8ikWAM';
  const url = `https://api.elevenlabs.io/v1/text-to-speech/${selectedVoice}`;

  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Accept': 'audio/mpeg',
      'Content-Type': 'application/json',
      'xi-api-key': apiKey
    },
    body: JSON.stringify({
      text: text,
      model_id: 'eleven_multilingual_v2',
      voice_settings: {
        stability: parseFloat(stability),
        similarity_boost: parseFloat(similarity),
        style: 0.15,
        use_speaker_boost: true
      }
    })
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    const message = errorData.detail?.message || `ElevenLabs API error: ${response.status} ${response.statusText}`;
    throw new Error(message);
  }

  const audioBlob = await response.blob();
  return URL.createObjectURL(audioBlob);
}

/**
 * Fallback Web Speech Synthesis for when ElevenLabs API key is not present
 */
export function speakWebSpeechFallback(text, onEndCallback) {
  if (!('speechSynthesis' in window)) {
    throw new Error('Web Speech API is not supported in this browser.');
  }

  window.speechSynthesis.cancel(); // Stop any active speech

  const utterance = new SpeechSynthesisUtterance(text);
  utterance.rate = 0.85; // Slower rate for calming effect
  utterance.pitch = 0.95; // Slightly lower, softer pitch
  utterance.volume = 1.0;

  // Try to find a soft English voice
  const voices = window.speechSynthesis.getVoices();
  const calmVoice = voices.find(v => 
    v.lang.startsWith('en') && (v.name.includes('Natural') || v.name.includes('Samantha') || v.name.includes('Google') || v.name.includes('Karen'))
  ) || voices.find(v => v.lang.startsWith('en'));

  if (calmVoice) {
    utterance.voice = calmVoice;
  }

  if (onEndCallback) {
    utterance.onend = onEndCallback;
  }

  window.speechSynthesis.speak(utterance);
}
