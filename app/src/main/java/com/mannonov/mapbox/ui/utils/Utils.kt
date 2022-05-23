package com.mannonov.mapbox.ui.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.mannonov.mapbox.ui.manager.MapManager
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions

fun AppCompatActivity.setFragment(fragment: Fragment, layout: Int) {
    supportFragmentManager.beginTransaction().replace(layout, fragment).commit()
}

fun Fragment.isPermissionGranted(): Boolean {
    return checkSelfPermission(
        requireActivity(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        requireActivity(),
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun Fragment.enableMyLocation(): Boolean {
    return if (isPermissionGranted()) {
        true
    } else {
        requestPermissions(
            arrayOf<String>(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            712
        )
        false
    }
}

fun MapManager.moveToPosition(lat: Double, lng: Double, zoom: Double): CameraOptions {
    return CameraOptions.Builder().center(Point.fromLngLat(lng, lat)).zoom(zoom).build()
}

