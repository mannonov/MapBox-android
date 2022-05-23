package com.mannonov.mapbox.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.mannonov.mapbox.R
import com.mannonov.mapbox.databinding.FragmentMapBinding
import com.mannonov.mapbox.ui.manager.MapManager
import com.mannonov.mapbox.ui.manager.MapManagerImpl
import com.mannonov.mapbox.ui.utils.CameraMoveListener
import com.mannonov.mapbox.ui.utils.LocatorStateListener
import com.mannonov.mapbox.ui.utils.enableMyLocation
import com.mapbox.geojson.Point
import kotlin.math.ln


class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding: FragmentMapBinding by viewBinding(FragmentMapBinding::bind)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapManager: MapManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapManager = MapManagerImpl(binding.mapView)
        enableMyLocation().run {
            if (this) {
                getCurrentLocation(fusedLocationClient)
            }
        }
        mapManager.setCameraMoveListener(moveListener = CameraMoveListener { lng, lat ->
            movedCameraLocation(lng = lng, lat = lat)
        })
        mapManager.setLocatorStateListener(stateListener = LocatorStateListener { newState ->
            changeLocatorState(newState)
        })
    }

    private fun movedCameraLocation(lng: Double, lat: Double) {
        Snackbar.make(binding.root,"lng = $lng, lat = $lat",Snackbar.LENGTH_SHORT).show()
    }

    private fun changeLocatorState(state: Boolean) {
        when (state) {
            true -> {
                binding.apply {
                    locatorLottie.playAnimation()
                    locatorLottie.loop(state)
                }
            }
            false -> {
                binding.apply {
                    locatorLottie.loop(state)
                }
            }
        }
    }

    private fun getCurrentLocation(fusedLocationClient: FusedLocationProviderClient) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                mapManager.moveToLocation(location = location)
                mapManager.addUserLocationIndicator(
                    Point.fromLngLat(
                        location.longitude,
                        location.latitude
                    )
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            712 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation(fusedLocationClient = fusedLocationClient)
                }
            }
        }
    }

}