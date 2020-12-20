package com.abbasibnilkham.timerapp.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.*
import android.os.Build
import com.abbasibnilkham.timerapp.utils.Constants.CHANNEL_TIME_RUNNING
import com.abbasibnilkham.timerapp.utils.Constants.CHANNEL_TIME_FINISHED

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelTimeRunning = NotificationChannel(CHANNEL_TIME_RUNNING, "Channel Time Running", IMPORTANCE_LOW)
            channelTimeRunning.description = "This is Timer Running Channel"
            val channelTimeFinished = NotificationChannel(CHANNEL_TIME_FINISHED, "Channel Time Finished", IMPORTANCE_HIGH)
            channelTimeFinished.description = "This is Timer Finished Channel"

            val manager = getSystemService(NotificationManager::class.java) as NotificationManager
            manager.createNotificationChannel(channelTimeRunning)
            manager.createNotificationChannel(channelTimeFinished)
        }
    }

}