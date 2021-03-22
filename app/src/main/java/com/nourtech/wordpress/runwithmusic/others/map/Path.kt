package com.nourtech.wordpress.runwithmusic.others.map

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class Path {

    lateinit var polylines: Polylines

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    init{


    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            polylines.apply {
                last().add(pos)
                //polylines = this
            }
        }
    }
}