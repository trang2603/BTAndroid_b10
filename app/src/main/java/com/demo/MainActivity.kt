package com.demo

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.demo.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                binding.granted.text = "Granted"
                binding.granted.setTextColor(getColor(R.color.green))
                binding.btnStart.setBackgroundColor(getColor(R.color.green))
                binding.btnStart.setOnClickListener {
                    binding.lat.visibility = View.VISIBLE
                    binding.lng.visibility = View.VISIBLE
                    getLastLocation()
                }
            } else {
                binding.granted.text = "Not granted"
                binding.granted.setTextColor(getColor(R.color.red))
                binding.lat.visibility = View.GONE
                binding.lng.visibility = View.GONE
                binding.btnStart.setBackgroundColor(getColor(R.color.gray)) // Disable button or change color to indicate inactive state
                binding.btnStart.setOnClickListener(null) // Remove the click listener if permission is not granted
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION,
            )
            binding.granted.text = "Not granted"
            binding.granted.setTextColor(getColor(R.color.red))
            binding.lat.visibility = View.GONE
            binding.lng.visibility = View.GONE
            binding.btnStart.setBackgroundColor(getColor(R.color.gray)) // Disable button or change color to indicate inactive state
            binding.btnStart.setOnClickListener(null)
        } else {
            binding.granted.text = "Granted"
            binding.granted.setTextColor(getColor(R.color.green))
            binding.btnStart.setBackgroundColor(getColor(R.color.green))
            binding.btnStart.setOnClickListener {
                binding.lat.visibility = View.VISIBLE
                binding.lng.visibility = View.VISIBLE
                getLastLocation()
            }
        }
    }

    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                binding.lat.text = "Lat: ${location.latitude}"
                binding.lng.text = "Lng: ${location.longitude}"
            } else {
                // Handle the case where location is null
                binding.lat.text = "Lat: Not available"
                binding.lng.text = "Lng: Not available"
            }
        }
    }
}
