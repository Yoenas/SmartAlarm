package com.yoenas.smartalarm.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yoenas.smartalarm.data.Alarm

@Dao
interface AlarmDAO {
    @Insert
    fun addAlarm(alarm : Alarm)

    @Query("SELECT * from alarm")
    fun getAlarm() : LiveData<List<Alarm>>

    @Delete
    fun deleteAlarm(alarm: Alarm)
}