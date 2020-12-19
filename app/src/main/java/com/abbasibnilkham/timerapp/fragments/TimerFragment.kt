package com.abbasibnilkham.timerapp.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abbasibnilkham.timerapp.MainActivity
import com.abbasibnilkham.timerapp.R
import com.abbasibnilkham.timerapp.adapters.TimerAdapter
import com.abbasibnilkham.timerapp.cache.PrefUtils
import com.abbasibnilkham.timerapp.dialogs.AddDialog
import com.abbasibnilkham.timerapp.models.Timer
import com.abbasibnilkham.timerapp.utils.Constants
import com.abbasibnilkham.timerapp.utils.Constants.ACTION_UPDATE_NOTIFICATION
import com.abbasibnilkham.timerapp.utils.Constants.NOTIFICATION_ID
import com.abbasibnilkham.timerapp.utils.Constants.PRIMARY_CHANNEL_ID
import com.abbasibnilkham.timerapp.utils.gone
import com.abbasibnilkham.timerapp.utils.visible
import com.abbasibnilkham.timerapp.viewmodels.TimerViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_timer.*

class TimerFragment : Fragment(R.layout.fragment_timer), AddDialog.OnAddButtonClickListener,
    TimerAdapter.OnTimerItemClickListener {

    private var dialog: AddDialog? = null
    private var adapter: TimerAdapter? = null
    private lateinit var mTimerViewModel: TimerViewModel
    private var countDownTimer: CountDownTimer? = null
    private var millisLeft: Long? = null
    private var millisStart: Long? = null
    private var endTime: Long? = null
    private var timerState = TimerState.Null
    private var mNotifyManager: NotificationManager? = null
    private var mReceiver = NotificationReceiver()

    enum class TimerState {
        Null, Stopped, Paused, Running, Finished
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TimerAdapter(this)
        rvTimers.layoutManager = LinearLayoutManager(requireContext())
        rvTimers.adapter = adapter
        mTimerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)
        mTimerViewModel.getAllTimers.observe(viewLifecycleOwner, { timers ->
            adapter?.setListTimer(timers)
        })
        btnAddTimeToCountdown.setOnClickListener {
            createDialog()
        }


        btnStartPauseTimer.setOnClickListener {
            if (timerState == TimerState.Running) {
                pauseTimer()
            } else {
                startOrResumeTimer()
            }
        }

        btnResumeRestart.setOnClickListener {
            if (timerState == TimerState.Finished) {
                resetTimer()
                cancelNotification()
            } else startOrResumeTimer()
        }

        btnCancel.setOnClickListener {
            timerState = TimerState.Null
            updateMainUI()
            updateButtonsAndTimerLayout()
            countDownTimer?.cancel()
            cancelNotification()
        }
    }

    override fun onStart() {
        super.onStart()

        timerState = PrefUtils.getTimerState(requireContext())
        updateMainUI()

        if (timerState != TimerState.Null) {
            millisStart = PrefUtils.getMillisStart(requireContext())
            millisLeft = millisStart
            if (timerState == TimerState.Stopped) updateCountDownTime(millisStart!!)
            else {
                when (timerState) {
                    TimerState.Running -> {
                        endTime = PrefUtils.getEndTime(requireContext())
                        millisLeft = endTime!! - System.currentTimeMillis()
                        if (millisLeft!! < 0) {
                            millisLeft = 0
                            timerState = TimerState.Finished
                        } else startOrResumeTimer()
                    }
                    TimerState.Finished -> {
                        millisLeft = 0
                    }
                    else -> {
                        millisLeft = PrefUtils.getMillisLeft(requireContext())
                    }
                }
            }
            updateCountDownTime(millisLeft!!)
            updateButtonsAndTimerLayout()
        }

        createNotificationChannel()
        requireContext().registerReceiver(mReceiver, IntentFilter(ACTION_UPDATE_NOTIFICATION))
    }

    override fun onStop() {
        super.onStop()
        PrefUtils.setTimerState(requireContext(), timerState)
        if (timerState != TimerState.Null) {
            PrefUtils.setMillisStart(requireContext(), millisStart!!)
            if (timerState != TimerState.Stopped) {
                when (timerState) {
                    TimerState.Running -> PrefUtils.setEndTime(requireContext(), endTime!!)
                    else -> PrefUtils.setMillisLeft(requireContext(), millisLeft!!)
                }
            }
        }
        if (countDownTimer != null) countDownTimer?.cancel()
    }

    private fun startOrResumeTimer() {
        endTime = System.currentTimeMillis() + millisLeft!!

        timerState = TimerState.Running
        updateButtonsAndTimerLayout()
        countDownTimer = object : CountDownTimer(millisLeft!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                millisLeft = millisUntilFinished
                updateCountDownTime(millisLeft!!)
            }

            override fun onFinish() {
                timerState = TimerState.Finished
                updateButtonsAndTimerLayout()
                sendNotification()
            }
        }.start()
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        timerState = TimerState.Paused
        updateButtonsAndTimerLayout()
    }

    private fun resetTimer() {
        millisLeft = millisStart
        updateCountDownTime(millisStart!!)
        timerState = TimerState.Stopped
        updateButtonsAndTimerLayout()
    }

    private fun updateMainUI() {
        if (timerState == TimerState.Null) {
            activity?.bnvTabs?.visible()
            rvTimers.visible()
            btnAddTimeToCountdown.visible()
        } else {
            rvTimers.gone()
            btnAddTimeToCountdown.gone()
            activity?.bnvTabs?.gone()
        }
    }

    private fun updateButtonsAndTimerLayout() {
        if (timerState == TimerState.Null) {
            btnStartPauseTimer.gone()
            llResumeRestartCancel.gone()
            llRunningTimer.gone()
        } else {
            if (timerState == TimerState.Stopped) {
                btnStartPauseTimer.visible()
                llResumeRestartCancel.gone()
                btnStartPauseTimer.text = "Start"
            } else {
                if (timerState == TimerState.Running) {
                    btnStartPauseTimer.text = "Pause"
                    btnStartPauseTimer.visible()
                    llResumeRestartCancel.gone()
                } else {
                    if (timerState == TimerState.Finished) btnResumeRestart.text = "Restart"
                    else btnResumeRestart.text = "Resume"
                    btnStartPauseTimer.gone()
                    llResumeRestartCancel.visible()
                }
            }
            llRunningTimer.visible()
        }
    }

    private fun updateCountDownTime(millis: Long) {
        val hour = ((millis / 1000) / 3600).toInt()
        val minute = (((millis / 1000) % 3600) / 60).toInt()
        val second = ((millis / 1000) % 60).toInt()
        tvRunningHour.text = if (hour > 9) hour.toString() else "0${hour}"
        tvRunningMinute.text =
            if (minute > 9) minute.toString() else "0${minute}"
        tvRunningSecond.text =
            if (second > 9) second.toString() else "0${second}"
        updateNotification(hour, minute, second)
    }

    private fun createDialog() {
        dialog = AddDialog(requireContext())
        dialog?.setListener(this)
        dialog?.show()
    }

    override fun onAddButtonClick(timer: Timer) {
        mTimerViewModel.insertTimer(timer)
        Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
        dialog?.dismiss()
    }

    override fun onItemClick(timer: Timer) {
        tvRunningHour.text = if (timer.hours > 9) timer.hours.toString() else "0${timer.hours}"
        tvRunningMinute.text =
            if (timer.minutes > 9) timer.minutes.toString() else "0${timer.minutes}"
        tvRunningSecond.text =
            if (timer.seconds > 9) timer.seconds.toString() else "0${timer.seconds}"

        millisStart = Constants.toMillis(timer.hours, timer.minutes, timer.seconds)
        millisLeft = millisStart
        timerState = TimerState.Stopped
        updateMainUI()
        updateButtonsAndTimerLayout()
    }

    fun createNotificationChannel() {
        mNotifyManager =
            requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Finished Notification",
                IMPORTANCE_DEFAULT
            ).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "Timer is up"
            }
            mNotifyManager?.createNotificationChannel(notificationChannel)
        }
    }

    fun getNotificationBuilder(
        hour: String,
        minute: String,
        second: String
    ): NotificationCompat.Builder {
        val notificationIntent = Intent(requireContext(), MainActivity::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(
            requireContext(),
            NOTIFICATION_ID,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notifyBuilder = NotificationCompat.Builder(requireContext(), PRIMARY_CHANNEL_ID)
            .setContentTitle("$hour:$minute:$second")
            .setContentText("You have been notified about time is up.")
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)

            .setDefaults(NotificationCompat.DEFAULT_ALL)
        return if (timerState == TimerState.Running)
            notifyBuilder.setPriority(NotificationCompat.PRIORITY_LOW)
        else notifyBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
        //return notifyBuilder
    }

    fun sendNotification() {
        val updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)
        val updatePendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            NOTIFICATION_ID,
            updateIntent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val notifyBuilder = getNotificationBuilder("nan", "nan", "nan")
            .addAction(R.drawable.ic_play, "Update notification", updatePendingIntent)
        mNotifyManager?.notify(NOTIFICATION_ID, notifyBuilder.build())
    }

    fun updateNotification(hour: Int, minute: Int, second: Int) {
        val hourString = if (hour > 9) hour.toString() else "0${hour}"
        val minuteString = if (minute > 9) minute.toString() else "0${minute}"
        val secondString = if (second > 9) second.toString() else "0${second}"
        val notifyBuilder = getNotificationBuilder(hourString, minuteString, secondString)
        mNotifyManager?.notify(NOTIFICATION_ID, notifyBuilder.build())
    }

    fun cancelNotification() {
        mNotifyManager?.cancel(NOTIFICATION_ID)
    }

    inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //updateNotification()
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireContext().unregisterReceiver(mReceiver)
    }
}