package com.example.aplikasiandroidpertama

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext // Wajib ditambahkan

class PendaftaranActivity : AppCompatActivity() {

    // ... (Deklarasi properti tetap sama) ...
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNamaDepan: EditText
    private lateinit var etNamaBelakang: EditText
    private lateinit var etPassword: EditText
    private lateinit var etUlangPassword: EditText
    private lateinit var btnKirim: Button
    private lateinit var btnBatal: Button
    private lateinit var tvPesan: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pendaftaran)

        initializeViews()
        setupListeners()

        btnBatal.setOnClickListener {
            val intenLogin = Intent(this, MainActivity::class.java)
            startActivity(intenLogin)
            finish()
        }

        tvPesan.text = "Mohon isi semua data pendaftaran."
        tvPesan.setTextColor(resources.getColor(android.R.color.holo_green_dark))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Pendaftaran)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializeViews() {
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etNamaDepan = findViewById(R.id.etNamaDepan)
        etNamaBelakang = findViewById(R.id.etNamaBelakang)
        etPassword = findViewById(R.id.etPassword)
        etUlangPassword = findViewById(R.id.etUlangPassword)
        btnKirim = findViewById(R.id.btnKirim)
        btnBatal = findViewById(R.id.btnBatal)
        tvPesan = findViewById(R.id.tvPesan)
    }

    private fun setupListeners() {
        btnKirim.setOnClickListener {
            handleRegistration()
        }
    }

    private fun handleRegistration() {

        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val namaDepan = etNamaDepan.text.toString().trim()
        val namaBelakang = etNamaBelakang.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val ulangPassword = etUlangPassword.text.toString().trim()

        // --- 1. VALIDASI DATA (HARUS BERADA PALING ATAS) ---

        if (username.isEmpty() || email.isEmpty() || namaDepan.isEmpty() || namaBelakang.isEmpty() || password.isEmpty() || ulangPassword.isEmpty()) {
            Toast.makeText(this, "Input Data Masih Kosong", Toast.LENGTH_LONG).show()
            tvPesan.text = "Gagal: Mohon lengkapi semua field!"
            tvPesan.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            return // Hentikan jika kosong
        }

        if (!username.isDigitsOnly()) {
            Toast.makeText(this, "Username harus berupa angka", Toast.LENGTH_LONG).show()
            return // Hentikan jika bukan angka
        }

        if (password != ulangPassword) {
            tvPesan.text = "Gagal: Password dan Konfirmasi Password tidak cocok!"
            tvPesan.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            return // Hentikan jika password tidak cocok
        }

        // --- 2. PENYIMPANAN DATA DAN NAVIGASI (HANYA JIKA VALIDASI LULUS) ---

        val userToSave = UserEntity(
            NamaDepan = namaDepan,
            NamaBelakang = namaBelakang,
            Username = username,
            Email = email,
            Password = password,
        )

        val db = AbsenDatabase.getDatabase(this)

        // Luncurkan Coroutine untuk Operasi I/O
        lifecycleScope.launch(Dispatchers.IO) {

            // 1. Simpan data dan Dapatkan ID (Long) dari DAO
            val newUserId = db.userDao().insert(userToSave)

            // Pindah ke Main Thread untuk Update UI dan Navigasi
            withContext(Dispatchers.Main) {

                // 2. Tampilan Sukses dan Bersihkan Field
                val namaLengkap = "$namaDepan $namaBelakang"
                Toast.makeText(this@PendaftaranActivity, "Pendaftaran Sukses! Selamat datang, $namaLengkap", Toast.LENGTH_LONG).show()
                tvPesan.text = "Pendaftaran Berhasil! Nama Anda: $namaLengkap"
                tvPesan.setTextColor(resources.getColor(android.R.color.holo_green_dark))
                clearFields()

                // 3. Kirim ID ke Dashboard (Wajib)
                val intentPindahDashboardActivity = Intent (this@PendaftaranActivity, DashboardActivity ::class.java)

                // PENTING: Mengirim ID yang benar sebagai Long (tipe data yang dikembalikan DAO)
                intentPindahDashboardActivity.putExtra("USER_ID", newUserId.toInt())


                startActivity(intentPindahDashboardActivity)
                finish()
            }
        }

        // Hapus SEMUA kode navigasi/Toast/clearFields yang ada di luar lifecycleScope.launch()
        // karena harus menunggu data disimpan terlebih dahulu.
    }

    private fun clearFields() {
        etUsername.setText("")
        etEmail.setText("")
        etNamaDepan.setText("")
        etNamaBelakang.setText("")
        etPassword.setText("")
        etUlangPassword.setText("")
    }
}