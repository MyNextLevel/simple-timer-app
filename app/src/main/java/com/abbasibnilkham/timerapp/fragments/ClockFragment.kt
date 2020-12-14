package com.abbasibnilkham.timerapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.abbasibnilkham.timerapp.R
import kotlinx.android.synthetic.main.fragment_clock.*

class ClockFragment : Fragment(R.layout.fragment_clock) {

    private var mTimerRunning:Boolean=false
    private var mTimeLeftInMillis:Long?=null
    private var mEndTime:Long?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnStartClock.setOnClickListener {

        }
    }

    private fun startTimer(){
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis!!
        cdvTimerClock.start(mTimeLeftInMillis!!)

        cdvTimerClock.setOnCountdownEndListener {

        }
    }

}