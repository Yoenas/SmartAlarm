package com.yoenas.smartalarm.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*


const val TIME_PICKER_TAG = "TimePicker"

fun timeFormatter(hourOfDay: Int, minute: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
    calendar.set(Calendar.MINUTE, minute)

    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
}

fun ImageView.loadImageDrawable(context: Context, source: Int?) {
    Glide.with(context)
        .load(source)
        .into(this)
}