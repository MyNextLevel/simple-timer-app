package com.abbasibnilkham.timerapp.utils

object Utils {
    const val SHARED_PREFS = "sharedPref"
    const val MILLIS_START_ID = "millisStartId"
    const val TIMER_STATE_ID = "timerStateId"
    const val END_TIME_ID = "endTimeId"
    const val MILLIS_LEFT_ID = "millisLeftId"
    const val ALARM_SET_TIME_ID = "alarmSetTimeId"

    fun toMillis(hours: Int, minutes: Int, seconds: Int): Long =
        ((hours * 3600 + minutes * 60 + seconds) * 1000).toLong()

}