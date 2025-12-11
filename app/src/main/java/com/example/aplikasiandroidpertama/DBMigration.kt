package com.example.aplikasiandroidpertama

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Ini adalah perintah SQL untuk menambahkan kolom 'password' ke tabel 'UserEntity'
        // dan memberikan nilai default kosong ('') karena kolom tersebut didefinisikan sebagai NOT NULL.
        database.execSQL("ALTER TABLE UserEntity ADD COLUMN password TEXT NOT NULL DEFAULT ''")
    }
}