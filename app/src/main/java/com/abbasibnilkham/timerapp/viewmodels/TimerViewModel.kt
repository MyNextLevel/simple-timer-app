package com.abbasibnilkham.timerapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.abbasibnilkham.timerapp.db.TimerDatabase
import com.abbasibnilkham.timerapp.repositories.TimerRepository
import com.abbasibnilkham.timerapp.models.Timer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimerViewModel(application:Application):AndroidViewModel(application) {

     val getAllTimers:LiveData<List<Timer>>
    private val repository: TimerRepository

    init {
        val timerDAO = TimerDatabase.getTimerDatabase(application).timerDao()
        repository = TimerRepository(timerDAO)
        getAllTimers = repository.getAllTimers
    }

    fun insertTimer(timer: Timer){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTimer(timer)
        }
    }

}