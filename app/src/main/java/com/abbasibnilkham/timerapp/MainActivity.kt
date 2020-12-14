package com.abbasibnilkham.timerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.abbasibnilkham.timerapp.cache.PrefUtils
import com.abbasibnilkham.timerapp.fragments.TimerFragment
import com.abbasibnilkham.timerapp.utils.gone
import com.abbasibnilkham.timerapp.utils.visible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bnvTabs.setupWithNavController(navHostFragment.findNavController())

        when (PrefUtils.getTimerState(applicationContext)) {
            TimerFragment.TimerState.Null -> showTabLayout()
            else -> hideTabLayout()
        }
    }

    private fun hideTabLayout() {
        bnvTabs.gone()
    }

    private fun showTabLayout() {
        bnvTabs.visible()
    }

}