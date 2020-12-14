package com.abbasibnilkham.timerapp.cache

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.abbasibnilkham.timerapp.fragments.TimerFragment
import com.abbasibnilkham.timerapp.utils.Utils.ALARM_SET_TIME_ID
import com.abbasibnilkham.timerapp.utils.Utils.END_TIME_ID
import com.abbasibnilkham.timerapp.utils.Utils.MILLIS_LEFT_ID
import com.abbasibnilkham.timerapp.utils.Utils.MILLIS_START_ID
import com.abbasibnilkham.timerapp.utils.Utils.SHARED_PREFS
import com.abbasibnilkham.timerapp.utils.Utils.TIMER_STATE_ID

class PrefUtils {

    companion object {

        fun getTimerState(context: Context): TimerFragment.TimerState {
            val prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            val ordinal = prefs.getInt(TIMER_STATE_ID, 0)
            return TimerFragment.TimerState.values()[ordinal]
        }

        fun setTimerState(context: Context, state: TimerFragment.TimerState) {
            val editor = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }


        fun getMillisStart(context: Context): Long {
            val prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            return prefs.getLong(MILLIS_START_ID, 0)
        }

        fun setMillisStart(context: Context, millisStart: Long) {
            val editor = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit()
            editor.putLong(MILLIS_START_ID, millisStart)
            editor.apply()
        }


        fun getEndTime(context: Context): Long {
            val prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            return prefs.getLong(END_TIME_ID, 0)
        }

        fun setEndTime(context: Context, endTime: Long) {
            val editor = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit()
            editor.putLong(END_TIME_ID, endTime)
            editor.apply()
        }


        fun getMillisLeft(context: Context): Long {
            val prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            return prefs.getLong(MILLIS_LEFT_ID, 0)
        }

        fun setMillisLeft(context: Context, millisLeft: Long) {
            val editor = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit()
            editor.putLong(MILLIS_LEFT_ID, millisLeft)
            editor.apply()
        }

    }
}