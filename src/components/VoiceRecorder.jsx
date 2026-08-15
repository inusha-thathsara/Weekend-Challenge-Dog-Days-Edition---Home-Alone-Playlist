import React, { useState, useRef } from 'react';
import { Mic, Square, Play, CheckCircle2 } from 'lucide-react';

export default function VoiceRecorder({ onVoiceRecorded, recordedAudioUrl }) {
  const [isRecording, setIsRecording] = useState(false);
  const [recordingTime, setRecordingTime] = useState(0);
  const mediaRecorderRef = useRef(null);
  const audioChunksRef = useRef([]);
  const timerRef = useRef(null);

  const startRecording = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      mediaRecorderRef.current = new MediaRecorder(stream);
      audioChunksRef.current = [];

      mediaRecorderRef.current.ondataavailable = (event) => {
        if (event.data.size > 0) audioChunksRef.current.push(event.data);
      };

      mediaRecorderRef.current.onstop = () => {
        const audioBlob = new Blob(audioChunksRef.current, { type: 'audio/webm' });
        onVoiceRecorded(URL.createObjectURL(audioBlob));
        stream.getTracks().forEach(track => track.stop());
      };

      mediaRecorderRef.current.start();
      setIsRecording(true);
      setRecordingTime(0);
      timerRef.current = setInterval(() => setRecordingTime(p => p + 1), 1000);
    } catch {
      alert('Microphone access denied or not supported.');
    }
  };

  const stopRecording = () => {
    if (mediaRecorderRef.current && isRecording) {
      mediaRecorderRef.current.stop();
      setIsRecording(false);
      clearInterval(timerRef.current);
    }
  };

  const handlePlay = () => {
    if (recordedAudioUrl) new Audio(recordedAudioUrl).play();
  };

  return (
    <div className="recorder-box">
      <div className="recorder-header">
        <span className="label">
          <Mic size={14} color="#f5a623" />
          Record Your Voice (Owner's Comfort Track)
        </span>
        {recordedAudioUrl && (
          <span className="recorded-badge">
            <CheckCircle2 size={13} /> Recorded
          </span>
        )}
      </div>

      <div className="recorder-controls">
        {!isRecording ? (
          <button onClick={startRecording} className="btn btn-record">
            <Mic size={15} />
            {recordedAudioUrl ? 'Re-record' : 'Start Recording'}
          </button>
        ) : (
          <button onClick={stopRecording} className="btn btn-glow" style={{ padding: '8px 18px', fontSize: 13 }}>
            <Square size={14} fill="#0a0e17" />
            Stop ({recordingTime}s)
          </button>
        )}

        {recordedAudioUrl && !isRecording && (
          <button onClick={handlePlay} className="btn btn-listen">
            <Play size={14} fill="#34d399" />
            Listen
          </button>
        )}
      </div>
    </div>
  );
}
