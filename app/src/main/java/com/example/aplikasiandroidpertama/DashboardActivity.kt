package com.example.aplikasiandroidpertama

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var tvNamaDepan: TextView
    private lateinit var tvNamaBelakang: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        val db = AbsenDatabase.getDatabase(this)
        userDao = db.userDao()

        // Mengambil ID yang dikirim dari Intent
        val userId = intent.getIntExtra("USER_ID", 0)

        tvNamaDepan = findViewById(R.id.tvNamaDepan)
        tvNamaBelakang = findViewById(R.id.tvNamaBelakang)
        tvUsername = findViewById(R.id.tvUsername)
        tvEmail = findViewById(R.id.tvEmail)

        if (userId != 0) {
            fetchUserData(userId)
        } else {
            Toast.makeText(this, "Gagal mendapatkan ID pengguna. ID 0 diterima.", Toast.LENGTH_LONG).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchUserData(userId: Int) {
        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                // Mengambil data pengguna berdasarkan ID yang dikirim
                userDao.getUserById(userId)
            }

            if (user != null) {
                // Menampilkan data
                tvUsername.text = user.Username
                tvEmail.text = user.Email
                tvNamaDepan.text = user.NamaDepan
                tvNamaBelakang.text = user.NamaBelakang
            } else {
                Toast.makeText(
                    this@DashboardActivity,
                    "Data pengguna tidak ditemukan untuk ID $userId.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}