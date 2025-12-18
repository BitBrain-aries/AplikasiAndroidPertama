package com.example.pertama

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration
import com.example.aplikasiandroidpertama.AttendanceDao
import com.example.aplikasiandroidpertama.AttendanceEntity
import com.example.aplikasiandroidpertama.MIGRATION_2_3
import com.example.aplikasiandroidpertama.UserDao
import com.example.aplikasiandroidpertama.UserEntity

@Database(entities = [UserEntity::class, AttendanceEntity::class], version = 3, exportSchema = false)
abstract class AbsenDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun attendanceDao(): AttendanceDao


    companion object {
        @Volatile
        private var INSTANCE: AbsenDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // TODO: tulis perubahan tabel di sini
            }
        }


        fun getDatabase(context: Context): AbsenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AbsenDatabase::class.java,
                    "aplikasiabsen"
                ).addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}