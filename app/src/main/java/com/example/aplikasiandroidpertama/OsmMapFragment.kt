package com.example.aplikasiandroidpertama

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pertama.AbsenDatabase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.tileprovider.tilesource.TileSourceFactory

class OsmMapFragment : Fragment() {

    private lateinit var map: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var db: AbsenDatabase

    private val LOCATION_PERMISSION_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AbsenDatabase.getDatabase(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val context = requireContext().applicationContext
        Configuration.getInstance().load(context, context.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE))
        Configuration.getInstance().setUserAgentValue("AplikasAbsen")

        requestPermissionsIfNecessary()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_osm_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        map = view.findViewById(R.id.osm_map_view)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), map)
        map.overlays.add(myLocationOverlay)

        setupMapAndLocation()

        val btnAbsen = view.findViewById<Button>(R.id.btnAbsen)
        btnAbsen.setOnClickListener {
            handleCheckIn() // Memanggil fungsi absen
        }
    }

    private fun handleCheckIn() {
        // Mengambil ID dari DashboardActivity (Pastikan variabel 'userId' di Activity publik atau gunakan Intent)
        val dashboardActivity = activity as? DashboardActivity
        val userId = dashboardActivity?.intent?.getIntExtra("USER_ID", 0) ?: 0

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "Izin Lokasi Belum Diberikan", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                // Melakukan operasi database di dalam Coroutine
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val attendance = AttendanceEntity(
                            userId = userId,
                            checkInTime = System.currentTimeMillis(),
                            latitude = location.latitude,
                            longitude = location.longitude
                        )

                        // Pindah ke Background Thread (IO) untuk insert ke DB
                        val resultId = withContext(Dispatchers.IO) {
                            db.attendanceDao().insertAttendance(attendance)
                            // Asumsi: insert mengembalikan Long (Row ID)
                        }

                        // Kembali ke Main Thread otomatis untuk update UI
                        if (resultId > 0) {
                            Toast.makeText(requireContext(), "Absen Berhasil", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(requireContext(), "Absen Gagal", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Gagal Mendapatkan Lokasi. Aktifkan GPS.", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // --- Fungsi Helper Tetap Sama ---
    private fun requestPermissionsIfNecessary() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val permissionsToRequest = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), permissionsToRequest.toTypedArray(), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun setupMapAndLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myLocationOverlay.enableFollowLocation()
            myLocationOverlay.enableMyLocation()
            centerMapOnUserLocation()
        } else {
            val defaultPoint = GeoPoint(-6.9034, 107.6175)
            map.controller.setCenter(defaultPoint)
            map.controller.setZoom(14.0)
        }
    }

    private fun centerMapOnUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val userGeoPoint = GeoPoint(it.latitude, it.longitude)
                map.controller.animateTo(userGeoPoint)
                map.controller.setZoom(16.0)
                map.invalidate()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
        setupMapAndLocation()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}