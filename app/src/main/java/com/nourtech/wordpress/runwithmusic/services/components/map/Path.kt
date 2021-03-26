package com.nourtech.wordpress.runwithmusic.services.components.map

import com.google.android.gms.maps.model.LatLng

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class Path {

    private var polylines: Polylines = mutableListOf(mutableListOf())

    fun addPathPoint(latLng: LatLng) {
        polylines.last().add(latLng)
    }
    fun isEmpty(): Boolean {
        return polylines.isNotEmpty() && polylines.last().isNotEmpty()
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