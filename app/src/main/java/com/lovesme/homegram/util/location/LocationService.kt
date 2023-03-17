package com.lovesme.homegram.util.location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleService
import com.google.android.gms.location.*
import com.lovesme.homegram.data.model.Location

class LocationService : LifecycleService() {
    private val locationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, INTERVAL_UNIT).build()
    }
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private lateinit var userLocation: Location
    private lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()
        getPersonLocation()
    }

    private fun getPersonLocation() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    userLocation = Location(location.latitude, location.longitude)

                    Log.d("Location", "${userLocation.latitude.toString()} , ${userLocation.longitude.toString()}")
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        userLocation = Location(location.latitude, location.longitude)

                        Log.d("Location", "${userLocation.latitude.toString()} , ${userLocation.longitude.toString()}")
                    }
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    companion object {
        const val INTERVAL_UNIT = 1000L
    }
}