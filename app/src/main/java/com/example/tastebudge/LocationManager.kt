package com.example.tastebudge


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class LocationManager(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    suspend fun getLastLocation(): Location? {
        val isGrantedPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return try {
            if (isGrantedPermission) {
                val lastLocation = fusedLocationClient.lastLocation.await()
                lastLocation
            } else {
                Log.e("LocationManager", "No permission")
                null
            }
        } catch (e: Exception) {
            Log.e("LocationManager", "Error getting location", e)
            null
        }
    }
}
