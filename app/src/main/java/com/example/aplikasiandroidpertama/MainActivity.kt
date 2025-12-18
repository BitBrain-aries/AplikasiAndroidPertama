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
import androidx.lifecycle.lifecycleScope
import com.example.pertama.AbsenDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 1. Inisialisasi Database
        val db = AbsenDatabase.getDatabase(this)
        userDao = db.userDao()

        // 2. Inisialisasi Views Lokal
        val inputusername = findViewById<EditText>(R.id.editTextUsername)
        val inputPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
        val buttonDaftar = findViewById<Button>(R.id.buttonDaftar)

        // 3. Listener untuk tombol KIRIM (Login)
        buttonSubmit.setOnClickListener {
            val username = inputusername.text.toString()
            val password = inputPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Username dan Password tidak boleh kosong",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = withContext(Dispatchers.IO) {
                    userDao.getUserByUsernameAndPassword(username, password)
                }

                if (user != null) {
                    // KOREKSI: Menggunakan user.username (asumsi huruf kecil di UserEntity)
                    Toast.makeText(
                        this@MainActivity,
                        "Login berhasil! Selamat datang, ${user.Username}",
                        Toast.LENGTH_LONG
                    ).show()

                    val intentDashboard = Intent(this@MainActivity, DashboardActivity::class.java)

                    // KOREKSI: Mengirim ID dengan user.id (asumsi huruf kecil di UserEntity)
                    intentDashboard.putExtra("USER_ID", user.id)

                    startActivity(intentDashboard)
                    finish()

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Login gagal. Username atau Password salah.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // 4. Listener untuk tombol DAFTAR
        buttonDaftar.setOnClickListener {
            val intentPindah = Intent(this, PendaftaranActivity::class.java)
            startActivity(intentPindah)
        }


        // Window Insets Handler
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}