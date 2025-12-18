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

val MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE attendances (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                user_id INTEGER NOT NULL,
                check_in_time INTEGER NOT NULL,
                latitude REAL NOT NULL,
                longitude REAL NOT NULL,
                
                -- Definisi FOREIGN KEY
                FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
            )
            """.trimIndent()
        )

        // Opsional: Membuat index pada user_id untuk performa query
        database.execSQL(
            "CREATE INDEX index_attendance_user_id ON attendances(user_id)"
        )
    }
}
