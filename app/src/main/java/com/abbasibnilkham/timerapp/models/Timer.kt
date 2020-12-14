package com.abbasibnilkham.timerapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "timer_table")
data class Timer(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val hours:Int,
    val minutes:Int,
    val seconds:Int
):Parcelable
