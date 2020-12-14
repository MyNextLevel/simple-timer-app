package com.abbasibnilkham.timerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abbasibnilkham.timerapp.R
import com.abbasibnilkham.timerapp.models.Timer
import kotlinx.android.synthetic.main.item_timer.view.*

class TimerAdapter(private val clickListener: OnTimerItemClickListener) :
    RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {

    private var listTimer = emptyList<Timer>()

    fun setListTimer(list: List<Timer>) {
        listTimer = list
        notifyDataSetChanged()
    }

    inner class TimerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                clickListener.onItemClick(listTimer[adapterPosition])
            }
        }
    }

    interface OnTimerItemClickListener {
        fun onItemClick(timer:Timer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        return TimerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_timer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        val item = listTimer[position]
        holder.itemView.tvHours.text =
            if (item.hours > 9) item.hours.toString() else "0${item.hours}"
        holder.itemView.tvMinutes.text =
            if (item.minutes > 9) item.minutes.toString() else "0${item.minutes}"
        holder.itemView.tvSeconds.text =
            if (item.seconds > 9) item.seconds.toString() else "0${item.seconds}"
    }

    override fun getItemCount(): Int = listTimer.size

}