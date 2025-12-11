package com.example.aplikasiandroidpertama

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    // KOREKSI UTAMA: Mengembalikan Long untuk mendapatkan ID yang dibuat Room.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserEntity): Long // <-- KOREKSI DI SINI!

    // Perhatian: Pastikan 'users' adalah nama tabel yang benar di UserEntity.kt
    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getUserByUsernameAndPassword(username: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userid")
    suspend fun getUserById(userid: Int): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?
}