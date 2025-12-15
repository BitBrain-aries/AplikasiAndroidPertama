package com.example.aplikasiandroidpertama

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface AttendanceDao {

    @Insert
    suspend fun interAttendance(attendance: AttendanceEntity): Long


}