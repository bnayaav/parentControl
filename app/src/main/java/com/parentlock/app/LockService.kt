package com.parentlock.app

import android.app.*
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationCompat

class LockService : Service() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("parentlock_channel", "ParentLock", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
        startForeground(1, NotificationCompat.Builder(this, "parentlock_channel")
            .setContentTitle("ParentLock פעיל")
            .setContentText("הגנת הורים מופעלת")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setOngoing(true).build())
    }
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY
}
