package com.example.aplikasiandroidpertama

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class AttendanceEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0


)
