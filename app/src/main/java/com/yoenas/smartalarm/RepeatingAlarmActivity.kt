package com.yoenas.smartalarm

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yoenas.smartalarm.data.Alarm
import com.yoenas.smartalarm.data.db.AlarmDB
import com.yoenas.smartalarm.databinding.ActivityRepeatingAlarmBinding
import com.yoenas.smartalarm.fragment.TimePickerFragment
import com.yoenas.smartalarm.utils.TIME_PICKER_TAG
import com.yoenas.smartalarm.utils.timeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepeatingAlarmActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private var alarmReceiver: AlarmReceiver? = null

    private var _binding: ActivityRepeatingAlarmBinding? = null
    private val binding get() = _binding as ActivityRepeatingAlarmBinding

    private val db by lazy { AlarmDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRepeatingAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReceiver()
        initView()
    }

    private fun initView() {
        binding.apply {
            btnSetTimeRepeating.setOnClickListener {
                val timePickerFragmentRepeating = TimePickerFragment()
                timePickerFragmentRepeating.show(supportFragmentManager, TIME_PICKER_TAG)
            }

            btnAddSetRepeatingAlarm.setOnClickListener {
                val time = tvRepeatingTime.text.toString()
                val message = edtNoteRepeating.text.toString()

                if (time != "Time") {
                    alarmReceiver?.setRepeatingAlarm(
                        applicationContext,
                        AlarmReceiver.TYPE_REPEATING,
                        time,
                        message
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        db.alarmDao().addAlarm(
                            Alarm(0, time, "Repeating Alarm", message, AlarmReceiver.TYPE_REPEATING)
                        )
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@RepeatingAlarmActivity,
                        "Alarm time has not yet been set.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            btnCancelSetRepeatingAlarm.setOnClickListener {
                finish()
            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        binding.tvRepeatingTime.text = timeFormatter(hourOfDay, minute)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}