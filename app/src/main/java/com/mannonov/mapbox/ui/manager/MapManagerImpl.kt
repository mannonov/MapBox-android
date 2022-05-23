package com.mannonov.mapbox.ui.manager

import android.location.Location
import com.mannonov.mapbox.ui.utils.CameraMoveListener
import com.mannonov.mapbox.ui.utils.LocatorStateListener
import com.mannonov.mapbox.ui.utils.moveToPosition
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.ScrollMode
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.gestures.getGesturesSettings
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar

class MapManagerImpl(private var map: MapView) : MapManager, OnMoveListener {

    private lateinit var cameraMoveListener: CameraMoveListener
    private lateinit var locatorStateListener: LocatorStateListener

    init {
        map.getMapboxMap().addOnMoveListener(this)
        map.gestures.rotateEnabled = false
        map.gestures.rotateDecelerationEnabled = true
        map.scalebar.enabled = false
        map.attribution.enabled = false
        map.logo.enabled = false
    }

    override fun moveToLocation(location: Location) {
        map.getMapboxMap().setCamera(
            this.moveToPosition(
                lat = location.latitude,
                lng = location.longitude,
                zoom = 14.0
            )
        )
    }

    override fun addUserLocationIndicator(point: Point) {
        map.gestures.focalPoint = map.getMapboxMap().pixelForCoordinate(point)
    }

    override fun setCameraMoveListener(moveListener: CameraMoveListener) {
        cameraMoveListener = moveListener
    }

    override fun setLocatorStateListener(stateListener: LocatorStateListener) {
        locatorStateListener = stateListener
    }

    override fun onMove(detector: MoveGestureDetector): Boolean {
        map.getMapboxMap().cameraState.center.apply {
            cameraMoveListener.onCameraMove(longitude(), latitude())
        }
        return true
    }

    override fun onMoveBegin(detector: MoveGestureDetector) {
        locatorStateListener.newState(true)
    }

    override fun onMoveEnd(detector: MoveGestureDetector) {
        locatorStateListener.newState(false)
    }

}