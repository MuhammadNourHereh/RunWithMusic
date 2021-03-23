package com.nourtech.wordpress.runwithmusic.services.components.map

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
    fun isEmpty(): Boolean{
        return polylines.isNotEmpty() && polylines.last().isNotEmpty()
    }
    fun last(): LatLng{
        return polylines.last().last()
    }


    fun addEmptyPolyline() = polylines.apply {
        add(mutableListOf())
    }
    fun hasAtLeastTowPoints(): Boolean{
       return polylines.isNotEmpty() && polylines.last().size > 1
    }
    fun getPreLastLatLng(): LatLng{
        return polylines.last()[polylines.last().size - 2]
    }
    fun getLastLatLng(): LatLng{
        return polylines.last().last()
    }
    fun getPolylines(): Polylines{
        return polylines
    }


}