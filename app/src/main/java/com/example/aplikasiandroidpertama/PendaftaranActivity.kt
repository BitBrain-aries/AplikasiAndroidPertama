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



class PendaftaranActivity : AppCompatActivity() {

    // 1. Deklarasi variabel untuk elemen UI (WAJIB DITAMBAHKAN)
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNamaDepan: EditText
    private lateinit var etNamaBelakang: EditText
    private lateinit var etPassword: EditText
    private lateinit var etUlangPassword: EditText
    private lateinit var btnKirim: Button // Sudah ada di onCreate, tapi diinisialisasi ulang di sini
    private lateinit var btnBatal: Button // Sudah ada di onCreate, tapi diinisialisasi ulang di sini
    private lateinit var tvPesan: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pendaftaran)

        // Pindahkan inisialisasi view ke fungsi terpisah
        initializeViews()
        setupListeners() // Tambahkan listener untuk btnKirim dan btnBatal (reset fields)

        // Hapus: val buttonkirim = findViewById<Button>(R.id.btnKirim)
        // Hapus: val backlogin = findViewById<Button>(R.id.btnBatal)
        // btnKirim dan btnBatal sudah diinisialisasi di initializeViews()

        // Listener btnBatal (kembali ke Login) - Dibiarkan di sini agar lebih jelas fungsinya
        btnBatal.setOnClickListener { // Menggunakan btnBatal yang sudah diinisialisasi
            val intenLogin = Intent(this, MainActivity::class.java) // Ganti MainActivity::class.java dengan class Login Anda
            startActivity(intenLogin)
            finish()
        }

        // Atur pesan awal
        tvPesan.text = "Mohon isi semua data pendaftaran."
        tvPesan.setTextColor(resources.getColor(android.R.color.holo_green_dark))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Pendaftaran)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // 2. Tambahkan fungsi inisialisasi View
    private fun initializeViews() {
        // Mendapatkan referensi menggunakan ID yang ada di activity_pendaftaran.xml
        // PASTIKAN ID DI activity_pendaftaran.xml SAMA DENGAN INI!
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etNamaDepan = findViewById(R.id.etNamaDepan)
        etNamaBelakang = findViewById(R.id.etNamaBelakang)
        etPassword = findViewById(R.id.etPassword)
        etUlangPassword = findViewById(R.id.etUlangPassword)
        btnKirim = findViewById(R.id.btnKirim)
        btnBatal = findViewById(R.id.btnBatal) // Diinisialisasi ulang
        tvPesan = findViewById(R.id.tvPesan)
    }

    // 3. Tambahkan fungsi setupListeners (hanya untuk btnKirim)
    private fun setupListeners() {
        btnKirim.setOnClickListener {
            handleRegistration()
        }
        // Listener btnBatal untuk reset field DIHAPUS, karena sudah digunakan untuk navigasi ke Login.
    }

    // 4. Tambahkan fungsi handleRegistration (LOGIKA UTAMA)
    private fun handleRegistration() {

        // Mengambil teks dari field input
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val namaDepan = etNamaDepan.text.toString().trim()
        val namaBelakang = etNamaBelakang.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val ulangPassword = etUlangPassword.text.toString().trim()

        if (!username.isDigitsOnly()) {
            Toast.makeText(this, "Username harus berupa angka", Toast.LENGTH_LONG).show()
            return
        }



        // 4a. Validasi: Memastikan SEMUA FIELD terisi (Tidak boleh kosong)
        if (username.isEmpty() || email.isEmpty() || namaDepan.isEmpty() || namaBelakang.isEmpty() || password.isEmpty() || ulangPassword.isEmpty()) {

            Toast.makeText(this, "Input Data Masih Kosong", Toast.LENGTH_LONG).show()

            tvPesan.text = "Gagal: Mohon lengkapi semua field!"
            tvPesan.setTextColor(resources.getColor(android.R.color.holo_red_dark))

            return // Menghentikan proses jika ada yang kosong
        }

        // 4b. Validasi: Password dan Konfirmasi Password harus sama
        if (password != ulangPassword) {
            tvPesan.text = "Gagal: Password dan Konfirmasi Password tidak cocok!"
            tvPesan.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            return // Menghentikan proses jika password tidak sama
        }

        // 5. NOTIFIKASI SUKSES (Nama Depan dan Nama Belakang digabung)
        val namaLengkap = "$namaDepan $namaBelakang"
        val message = "Pendaftaran Sukses!\nSelamat datang, $namaLengkap"

        Toast.makeText(this, message, Toast.LENGTH_LONG).show() // Menampilkan Notifikasi

        // Menampilkan pesan sukses di TextView
        tvPesan.text = "Pendaftaran Berhasil! Nama Anda: $namaLengkap"
        tvPesan.setTextColor(resources.getColor(android.R.color.holo_green_dark))

        val intentPindahDashboardActivity = Intent (this, DashboardActivity ::class.java)

        intentPindahDashboardActivity.putExtra("NAMA_DEPAN", namaDepan)
        intentPindahDashboardActivity.putExtra( "NAMA_BELAKANG", namaBelakang)
        intentPindahDashboardActivity. putExtra("Username", username.toInt())
        intentPindahDashboardActivity.putExtra("EMAIL", email)

        // *** BAGIAN INI YANG HILANG ***
        startActivity(intentPindahDashboardActivity)
        finish() // Menutup Pendaftaran Activity agar tidak bisa di-back

        // Mengosongkan field setelah sukses
        clearFields()
    }

    // 5. Tambahkan fungsi clearFields
    private fun clearFields() {
        etUsername.setText("")
        etEmail.setText("")
        etNamaDepan.setText("")
        etNamaBelakang.setText("")
        etPassword.setText("")
        etUlangPassword.setText("")
    }
}