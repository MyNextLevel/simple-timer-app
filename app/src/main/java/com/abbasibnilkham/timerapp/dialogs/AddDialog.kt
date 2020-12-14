package com.abbasibnilkham.timerapp.dialogs

import android.app.Dialog
import android.content.Context
import android.text.InputFilter
import android.view.Window
import com.abbasibnilkham.timerapp.R
import com.abbasibnilkham.timerapp.models.Timer
import kotlinx.android.synthetic.main.dialog_add_timer.*


class AddDialog(context: Context) : Dialog(context) {

    private var listener: OnAddButtonClickListener? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_add_timer)

        btnAddNewTimer.setOnClickListener {
            val item = Timer(0,etHours.text.toString().toInt(),etMinutes.text.toString().toInt(),etSeconds.text.toString().toInt())
            listener!!.onAddButtonClick(item)
        }
    }

    fun setListener(listener: OnAddButtonClickListener){
        this.listener = listener
    }

    interface OnAddButtonClickListener {
        fun onAddButtonClick(timer: Timer)
    }

}