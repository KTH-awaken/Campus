package com.google.firebase.codelab.friendlychat.data.sensors

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity

import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

import com.google.android.gms.location.LocationServices

class GpsManager(private val activity: ComponentActivity, private val onLocationUpdate: (Location) -> Unit) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var PERMISSIONS_FINE_LOCATION = 99

    private lateinit var locationCallback: LocationCallback

    private val requestPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                updateAccurateGPS()
                //updateGPS()
            } else {
                Log.e("GPSManager", "Location permission denied")
            }
        }

    fun requestLocationUpdates() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun updateGPS() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        Log.d("GPSManager", "Location = ${location.toString()}")
                        onLocationUpdate.invoke(location)
                    } else {
                        Log.e("GPSManager", "Last known location is null")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("GPSManager", "Error getting last known location", e)
                }
        } else {
            Log.d("GPSManager", "Requesting permissions...")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                activity.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ), PERMISSIONS_FINE_LOCATION
                )
            } else {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                Log.e("GPSManager", "Failed to get permissions for location tracking")
            }
        }
    }

    private fun updateAccurateGPS(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = createLocationRequest()
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    p0 ?: return
                    for (location in p0.locations){
                        Log.d("GPSManager", "Accurate location = ${location.toString()}")
                        onLocationUpdate.invoke(location)
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            Log.d("GPSManager", "Requesting permissions...")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                activity.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ), PERMISSIONS_FINE_LOCATION
                )
            } else {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                Log.e("GPSManager", "Failed to get permissions for location tracking")
            }
        }
    }
    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000  // 10 seconds
            fastestInterval = 5000  // 5 seconds
        }
    }
}