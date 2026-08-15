package com.pawspeace.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.pawspeace.MainActivity
import com.pawspeace.R
import com.pawspeace.audio.AudioSynthesizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private var exoPlayer: ExoPlayer? = null
    private val synthesizer = AudioSynthesizer()

    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private var timerJob: Job? = null
    private var voiceRepeatJob: Job? = null

    companion object {
        const val CHANNEL_ID = "paws_peace_playback_channel"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val EXTRA_VOICE_URI = "EXTRA_VOICE_URI"
        const val EXTRA_DURATION_MIN = "EXTRA_DURATION_MIN"
        const val EXTRA_REPEAT_MIN = "EXTRA_REPEAT_MIN"
        const val EXTRA_AMBIANCE_TYPE = "EXTRA_AMBIANCE_TYPE"
        const val EXTRA_DELAY_MIN = "EXTRA_DELAY_MIN"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        exoPlayer = ExoPlayer.Builder(this).build()
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        mediaSession = MediaSession.Builder(this, exoPlayer!!)
            .setSessionActivity(pendingIntent)
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when (intent?.action) {
            ACTION_START -> {
                val voiceUri = intent.getStringExtra(EXTRA_VOICE_URI)
                val durationMin = intent.getIntExtra(EXTRA_DURATION_MIN, 30)
                val repeatMin = intent.getIntExtra(EXTRA_REPEAT_MIN, 3)
                val ambianceType = intent.getStringExtra(EXTRA_AMBIANCE_TYPE) ?: "rain"
                val delayMin = intent.getIntExtra(EXTRA_DELAY_MIN, 0)

                startForeground(NOTIFICATION_ID, buildNotification("Soothing your pup... 🐾"))
                startPlaybackWorkflow(voiceUri, durationMin, repeatMin, ambianceType, delayMin)
            }
            ACTION_STOP -> {
                stopPlayback()
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun startPlaybackWorkflow(
        voiceUri: String?,
        durationMin: Int,
        repeatMin: Int,
        ambianceType: String,
        delayMin: Int
    ) {
        serviceScope.launch {
            if (delayMin > 0) {
                delay(delayMin * 60 * 1000L)
            }

            // Start Ambiance
            if (ambianceType == "rain" || ambianceType == "all") {
                synthesizer.startRain(0.5f)
            }
            if (ambianceType == "drone" || ambianceType == "all") {
                synthesizer.startDrone(0.4f)
            }

            // Start Voice Playback
            if (!voiceUri.isNullOrEmpty()) {
                playVoiceTrack(voiceUri)
                if (repeatMin > 0) {
                    voiceRepeatJob = launch {
                        while (isActive) {
                            delay(repeatMin * 60 * 1000L)
                            playVoiceTrack(voiceUri)
                        }
                    }
                }
            }

            // Session Countdown & Auto-Stop
            if (durationMin > 0) {
                timerJob = launch {
                    delay(durationMin * 60 * 1000L)
                    stopPlayback()
                    stopSelf()
                }
            }
        }
    }

    private fun playVoiceTrack(uri: String) {
        exoPlayer?.apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            play()
        }
    }

    private fun stopPlayback() {
        timerJob?.cancel()
        voiceRepeatJob?.cancel()
        synthesizer.stopAll()
        exoPlayer?.stop()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.notification_channel_desc)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(status: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Paws & Peace 🐾")
            .setContentText(status)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        stopPlayback()
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
