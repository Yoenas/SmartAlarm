package com.yoenas.smartalarm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yoenas.smartalarm.R
import com.yoenas.smartalarm.data.Alarm
import com.yoenas.smartalarm.databinding.RowItemAlarmBinding
import com.yoenas.smartalarm.utils.loadImageDrawable

class AlarmAdapter :
    RecyclerView.Adapter<AlarmAdapter.MyViewHolder>() {

    var listAlarm = ArrayList<Alarm>()

    inner class MyViewHolder(val binding: RowItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        RowItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val alarm = listAlarm[position]
        holder.binding.apply {
            tvTimeAlarm.text = alarm.time
            tvDateAlarm.text = alarm.date
            tvNoteAlarm.text = alarm.note
            when (alarm.type) {
                0 -> imgOneTime.loadImageDrawable(imgOneTime.context, R.drawable.ic_one_time)
                1 -> imgOneTime.loadImageDrawable(imgOneTime.context, R.drawable.ic_repeating)
            }
        }
    }

    override fun getItemCount() = listAlarm.size

    fun setData(newListAlarm: List<Alarm>) {
        val alarmDiffUtil = AlarmDiffUtil(listAlarm, newListAlarm)
        val alarmDiffUtilResult = DiffUtil.calculateDiff(alarmDiffUtil)
        listAlarm.clear()
        listAlarm.addAll(newListAlarm)
        alarmDiffUtilResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}
