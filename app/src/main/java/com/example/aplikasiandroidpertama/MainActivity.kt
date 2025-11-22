package com.example.aplikasiandroidpertama

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inisialisasi Views
        val inputusername = findViewById<EditText>(R.id.editTextUsername)
        val inputPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
        val buttonDaftar = findViewById<Button>(R.id.buttonDaftar) // Pindahkan inisialisasi di sini

        // Listener untuk tombol KIRIM (Login)
        buttonSubmit.setOnClickListener {
            val username = inputusername.text.toString()
            val password = inputPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) { // Tambahkan cek untuk password juga
                Toast.makeText(
                    this,
                    "Username/Password tidak boleh kosong",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Proses login..",
                    Toast.LENGTH_LONG
                ).show()
                // Lakukan logika login di sini
            }
        }

        // Listener untuk tombol DAFTAR
        // Pindahkan di luar buttonSubmit.setOnClickListener
        buttonDaftar.setOnClickListener {
            val intentPindah = Intent(this, PendaftaranActivity :: class.java)
            startActivity(intentPindah)
            // finish() // Hati-hati menggunakan finish() di sini, karena akan menutup MainActivity
        }


        // Window Insets Handler (tetap di dalam onCreate)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}