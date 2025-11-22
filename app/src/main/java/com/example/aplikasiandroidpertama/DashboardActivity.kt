package com.example.aplikasiandroidpertama

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        val Nama_Depan = intent.getStringExtra("NAMA_DEPAN")
        val Nama_Belakang = intent.getStringExtra("NAMA_BELAKANG")
        val Username = intent.getIntExtra("Username", 0)
        val Email = intent.getStringExtra("EMAIL")

        val tvNamaDepan = findViewById<TextView>(R.id.tvNamaDepan)
        val tvNamaBelakang = findViewById<TextView>(R.id.tvNamaBelakang)
        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)

        tvNamaDepan.text = Nama_Depan
        tvNamaBelakang.text = Nama_Belakang
        tvUsername.text = Username.toString()
        tvEmail.text = Email




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}