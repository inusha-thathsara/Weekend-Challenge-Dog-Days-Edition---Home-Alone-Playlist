package com.pawspeace.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

class AudioSynthesizer {

    private var rainTrack: AudioTrack? = null
    private var droneTrack: AudioTrack? = null
    private var heartbeatJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    private val sampleRate = 44100

    fun startRain(volume: Float) {
        stopRain()
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        rainTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        rainTrack?.setVolume(volume)
        rainTrack?.play()

        scope.launch {
            val buffer = ShortArray(bufferSize / 2)
            var b0 = 0.0; var b1 = 0.0; var b2 = 0.0; var b3 = 0.0; var b4 = 0.0; var b5 = 0.0
            while (isActive && rainTrack?.playState == AudioTrack.PLAYSTATE_PLAYING) {
                for (i in buffer.indices) {
                    val white = Random.nextDouble(-1.0, 1.0)
                    b0 = 0.99886 * b0 + white * 0.0555179
                    b1 = 0.99332 * b1 + white * 0.0750759
                    b2 = 0.96900 * b2 + white * 0.1538520
                    b3 = 0.86650 * b3 + white * 0.3104856
                    b4 = 0.55000 * b4 + white * 0.5329522
                    b5 = -0.7616 * b5 - white * 0.0168980
                    val pink = (b0 + b1 + b2 + b3 + b4 + b5 + white * 0.5362) * 0.06
                    buffer[i] = (pink * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }
                rainTrack?.write(buffer, 0, buffer.size)
            }
        }
    }

    fun stopRain() {
        try {
            rainTrack?.stop()
            rainTrack?.release()
        } catch (_: Exception) {}
        rainTrack = null
    }

    fun startDrone(volume: Float) {
        stopDrone()
        val bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT)
        droneTrack = AudioTrack.Builder()
            .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
            .setAudioFormat(AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(sampleRate).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        droneTrack?.setVolume(volume * 0.6f)
        droneTrack?.play()

        scope.launch {
            val buffer = ShortArray(bufferSize / 2)
            var phase = 0.0
            val freq = 216.0 // A3 432Hz subharmonic
            val inc = (2 * PI * freq) / sampleRate

            while (isActive && droneTrack?.playState == AudioTrack.PLAYSTATE_PLAYING) {
                for (i in buffer.indices) {
                    val sample = sin(phase) * 0.5
                    phase += inc
                    if (phase > 2 * PI) phase -= 2 * PI
                    buffer[i] = (sample * Short.MAX_VALUE).toInt().toShort()
                }
                droneTrack?.write(buffer, 0, buffer.size)
            }
        }
    }

    fun stopDrone() {
        try {
            droneTrack?.stop()
            droneTrack?.release()
        } catch (_: Exception) {}
        droneTrack = null
    }

    fun stopAll() {
        stopRain()
        stopDrone()
        heartbeatJob?.cancel()
    }
}
