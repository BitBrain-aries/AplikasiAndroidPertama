package com.example.aplikasiandroidpertama

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    // Fungsi untuk memasukan data
    // suspend membuatnya berjalan secara asinkron

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserEntity)

    // Contoh : Mendapatkan semua data
    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): List<UserEntity>


}