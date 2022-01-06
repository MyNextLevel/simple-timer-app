package com.abbasibnilkham.timerapp

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.abbasibnilkham.timerapp.utils.Constants
import com.abbasibnilkham.timerapp.utils.Constants.CHANNEL_TIME_FINISHED
import com.abbasibnilkham.timerapp.utils.Constants.CHANNEL_TIME_RUNNING

class MainActivity : AppCompatActivity() {

    private var notificationManager: NotificationManagerCompat? = null
    private var tvCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = NotificationManagerCompat.from(this)

        tvCount = findViewById(R.id.tvTimer)
    }

    fun startTimer(view: View) {
        var startTime = Constants.TEMP_TIME.toLong()
        val countDownTimer = object : CountDownTimer(startTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                startTime -= 1000
                tvCount?.text = "${startTime / 1000}"
                sendCountNotification(startTime)
            }

            override fun onFinish() {
                sendFinishNotification()
            }
        }
        countDownTimer.start()
    }

    private fun sendCountNotification(time: Long) {
        val textTimer = "${time/1000}"
        val notification = NotificationCompat.Builder(this, CHANNEL_TIME_RUNNING)
            .setContentTitle(textTimer)
            .setSmallIcon(R.drawable.ic_pause)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        notificationManager?.notify(1, notification)
    }

    private fun sendFinishNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_TIME_FINISHED)
            .setContentTitle("0")
            .setSmallIcon(R.drawable.ic_play)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentText("Time is finished")
            .build()
        notificationManager?.notify(1, notification)
    }
}