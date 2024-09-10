package com.example.boundedservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyService : Service() {
    private val binder = LocalBinder()
    private val CHANNEL_ID = "ServiceChannel"
    private val NOTIFICATION_ID = 1
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.vikram_rolex_bgm)
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, getNotification())
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel"
            val descriptionText = "Service notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Music Service")
            .setContentText("Song playing in background")
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    fun playMusic() {
        mediaPlayer?.start()
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    inner class LocalBinder : Binder() {
        fun getService(): MyService = this@MyService
    }
}