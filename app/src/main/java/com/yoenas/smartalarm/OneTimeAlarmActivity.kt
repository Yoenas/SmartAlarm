package com.yoenas.smartalarm

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yoenas.smartalarm.data.Alarm
import com.yoenas.smartalarm.data.db.AlarmDB
import com.yoenas.smartalarm.databinding.ActivityOneTimeAlarmBinding
import com.yoenas.smartalarm.fragment.DatePickerFragment
import com.yoenas.smartalarm.fragment.TimePickerFragment
import com.yoenas.smartalarm.utils.TIME_PICKER_TAG
import com.yoenas.smartalarm.utils.timeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class OneTimeAlarmActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener,
    TimePickerFragment.DialogTimeListener {

    private var alarmReceiver: AlarmReceiver? = null

    private var _binding: ActivityOneTimeAlarmBinding? = null
    private val binding get() = _binding as ActivityOneTimeAlarmBinding

    private val db by lazy { AlarmDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOneTimeAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReceiver()
        initView()
    }

    private fun initView() {
        binding.apply {
            btnSetDateOneTime.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, "DatePicker")
            }

            btnSetTimeOneTime.setOnClickListener {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TIME_PICKER_TAG)
            }

            btnAddSetOneTimeAlarm.setOnClickListener {
                val date = binding.tvOnceDate.text.toString()
                val time = binding.tvOnceTime.text.toString()
                val message = binding.edtNoteOneTime.text.toString()

                if (date != "Date" && time != "Time") {
                    alarmReceiver?.setOneTimeAlarm(
                        this@OneTimeAlarmActivity,
                        AlarmReceiver.TYPE_ONE_TIME,
                        date,
                        time,
                        message
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        db.alarmDao().addAlarm(Alarm(0, time, date, message, AlarmReceiver.TYPE_ONE_TIME))
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@OneTimeAlarmActivity,
                        "Set the Date & Time.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnCancelSetOneTimeAlarm.setOnClickListener {
                finish()
            }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        binding.tvOnceDate.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        binding.tvOnceTime.text = timeFormatter(hourOfDay, minute)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}