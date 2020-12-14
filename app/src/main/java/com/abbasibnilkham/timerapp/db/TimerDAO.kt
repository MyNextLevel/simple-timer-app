package com.abbasibnilkham.timerapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abbasibnilkham.timerapp.models.Timer

@Dao
interface TimerDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTimer(timer: Timer)

    @Query("SELECT * FROM timer_table ORDER BY id ASC")
    fun getTimers(): LiveData<List<Timer>>

}