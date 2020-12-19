package com.abbasibnilkham.timerapp.utils

object Constants {
    const val SHARED_PREFS = "sharedPref"
    const val MILLIS_START_ID = "millisStartId"
    const val TIMER_STATE_ID = "timerStateId"
    const val END_TIME_ID = "endTimeId"
    const val MILLIS_LEFT_ID = "millisLeftId"
    const val ALARM_SET_TIME_ID = "alarmSetTimeId"

    fun toMillis(hours: Int, minutes: Int, seconds: Int): Long =
        ((hours * 3600 + minutes * 60 + seconds) * 1000).toLong()

    const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    const val NOTIFICATION_ID = 0
    const val ACTION_UPDATE_NOTIFICATION = "com.abbasibnilkham.timerapp.utils.ACTION_UPDATE_NOTIFICATION"
}