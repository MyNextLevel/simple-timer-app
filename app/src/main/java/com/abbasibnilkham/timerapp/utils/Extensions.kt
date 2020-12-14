package com.abbasibnilkham.timerapp.utils

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

fun View.gone() {
    visibility = GONE
}

fun View.visible() {
    visibility = VISIBLE
}