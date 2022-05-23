package com.mannonov.mapbox.ui.manager

import android.location.Location
import com.mannonov.mapbox.ui.utils.CameraMoveListener
import com.mannonov.mapbox.ui.utils.LocatorStateListener
import com.mapbox.geojson.Point

interface MapManager {

    fun moveToLocation(location: Location)

    fun addUserLocationIndicator(point:Point)

    fun setCameraMoveListener(moveListener: CameraMoveListener)

    fun setLocatorStateListener(stateListener:LocatorStateListener)

}