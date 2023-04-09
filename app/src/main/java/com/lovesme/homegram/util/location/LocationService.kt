package com.lovesme.homegram.util.location

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.lovesme.homegram.data.model.Location
import com.lovesme.homegram.ui.main.MainActivity
import com.lovesme.homegram.R
import com.lovesme.homegram.data.repository.LocationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationService() : LifecycleService() {

    @Inject
    lateinit var repository: LocationRepository

    private val locationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, INTERVAL_UNIT).build()
    }
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private val notificationManager by lazy {
        this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val _userLocation: MutableStateFlow<Location> = MutableStateFlow(Location())
    private val userLocation: StateFlow<Location> = _userLocation

    private lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()
        setForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    private fun setForeground() {
        createNotification()
        getPersonLocation()
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pendingIntent: PendingIntent =
                Intent(this, MainActivity::class.java).let { notificationIntent ->
                    PendingIntent.getActivity(
                        this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                }
            val channelId = applicationContext.getString(R.string.location_notification_channel_id)
            val notification: Notification = Notification.Builder(this, channelId)
                .setContentTitle(applicationContext.getString(R.string.notification_title))
                .setContentText(applicationContext.getString(R.string.notification_message))
                .setSmallIcon(R.drawable.pet_normal_origin)
                .setContentIntent(pendingIntent)
                .setTicker(applicationContext.getString(R.string.notification_title))
                .build()
            createChannel(channelId)
            startForeground(82, notification)
        }
    }

    private fun getPersonLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        setObserver()

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    _userLocation.value = Location(location.latitude, location.longitude)
                    Log.d(
                        "Personal Location Test 1",
                        "${location.latitude} , ${location.longitude}"
                    )
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        _userLocation.value = Location(location.latitude, location.longitude)

                        Log.d(
                            "Personal Location Test 2",
                            "${location.latitude} , ${location.longitude}"
                        )
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

    private fun createChannel(id: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(id) == null) {
                val name = this.getString(R.string.location_notification_channel_name)
                NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                    notificationManager.createNotificationChannel(this)
                }
            }
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            userLocation.collectLatest { location ->
                delay(1_000L)
                repository.setLocation(location)
            }
        }
    }

    companion object {
        const val INTERVAL_UNIT = 1000L
    }
}