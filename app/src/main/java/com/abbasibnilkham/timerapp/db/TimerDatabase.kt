package com.abbasibnilkham.timerapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abbasibnilkham.timerapp.models.Timer

@Database(entities = [Timer::class], version = 2, exportSchema = false)
abstract class TimerDatabase : RoomDatabase() {

    abstract fun timerDao(): TimerDAO

    companion object {
        @Volatile
        private var INSTANCE: TimerDatabase? = null

        fun getTimerDatabase(context: Context): TimerDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimerDatabase::class.java,
                    "timer_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}