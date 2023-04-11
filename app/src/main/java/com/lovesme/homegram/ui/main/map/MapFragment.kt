package com.lovesme.homegram.ui.main.map

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lovesme.homegram.data.model.Location
import com.lovesme.homegram.databinding.FragmentMapBinding
import com.lovesme.homegram.ui.viewmodel.MapViewModel
import com.lovesme.homegram.util.location.LocationService
import kotlinx.coroutines.launch


class MapFragment : Fragment(), OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var locationServiceIntent: Intent

    private val mapViewModel: MapViewModel by activityViewModels()
    var isFirst = true

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            (permissions[Manifest.permission.ACCESS_FINE_LOCATION]
                ?: false) && (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false) -> {
                checkLocationPermission()
            }
            else -> {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocationService()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel.loadLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        enableMyLocation()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mapViewModel.locations.collect { locations ->
                        drawMembersMarkers(locations)
                    }
                }
            }
        }
    }

    private fun setLocationService() {
        locationServiceIntent = Intent(requireActivity(), LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(locationServiceIntent)
        } else {
            requireActivity().startService(locationServiceIntent)
        }
    }

    private fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            ) {
                setPermissionDialog(requireActivity())
            }
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            return
        }

        locationPermissionRequest.launch(PERMISSIONS)
    }

    private fun setPermissionDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.")

        val listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE ->
                    setBackgroundPermission()
            }
        }
        builder.setPositiveButton("네", listener)
        builder.setNegativeButton("아니오", null)

        builder.show()
    }

    private fun setBackgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                ), 2
            )
        }
    }

    private fun drawMembersMarkers(locations: List<Location>) {
        map.clear()
        for (item in locations) {
            val latLng = LatLng(item.latitude, item.longitude)
            val marker = if (item.title == mapViewModel.name) {
                if (isFirst) {
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            latLng, 10f
                        )
                    )
                    isFirst = false
                }

                MarkerOptions()
                    .position(latLng)
                    .title(item.title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            } else {
                MarkerOptions()
                    .position(latLng)
                    .title(item.title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
            }

            map.addMarker(marker)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    companion object {
        private val PERMISSIONS =
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
    }
}