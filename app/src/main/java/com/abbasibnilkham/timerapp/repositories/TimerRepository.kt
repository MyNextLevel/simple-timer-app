package com.abbasibnilkham.timerapp.repositories

import androidx.lifecycle.LiveData
import com.abbasibnilkham.timerapp.db.TimerDAO
import com.abbasibnilkham.timerapp.models.Timer

class TimerRepository(private val timerDAO: TimerDAO) {

    val getAllTimers : LiveData<List<Timer>> = timerDAO.getTimers()

    suspend fun insertTimer(timer: Timer){
        timerDAO.insertTimer(timer)
    }

}