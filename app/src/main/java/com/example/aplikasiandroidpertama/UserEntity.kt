package com.example.aplikasiandroidpertama

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Users")
data class UserEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "fristname")
    val NamaDepan: String,

    @ColumnInfo(name = "lastname")
    val NamaBelakang: String,

    val Username : String,

    val Email : String,
)
