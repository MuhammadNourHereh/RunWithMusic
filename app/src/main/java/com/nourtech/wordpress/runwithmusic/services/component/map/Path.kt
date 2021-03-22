package com.nourtech.wordpress.runwithmusic.services.component.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class Path {

    private var polylines : Polylines = mutableListOf(mutableListOf())

    fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            polylines.apply {
                last().add(pos)
            }
        }
    }
}