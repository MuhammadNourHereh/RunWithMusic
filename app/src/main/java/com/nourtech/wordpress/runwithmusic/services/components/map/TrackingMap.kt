package com.nourtech.wordpress.runwithmusic.services.components.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.nourtech.wordpress.runwithmusic.others.Constants.FASTEST_LOCATION_INTERVAL
import com.nourtech.wordpress.runwithmusic.others.Constants.LOCATION_UPDATE_INTERVAL
import com.nourtech.wordpress.runwithmusic.others.Constants.MAP_ZOOM
import com.nourtech.wordpress.runwithmusic.others.Constants.POLYLINE_COLOR
import com.nourtech.wordpress.runwithmusic.others.Constants.POLYLINE_WIDTH
import com.nourtech.wordpress.runwithmusic.others.TrackingUtility
import timber.log.Timber


class TrackingMap(val app: Context) {

    private val mPath = MutableLiveData<Path>()
    val path: LiveData<Path>
        get() = mPath

    private var isTracking = false
    private var map: GoogleMap? = null
    private val fusedLocationProviderClient = FusedLocationProviderClient(app)
    init {
        mPath.postValue(Path())
    }

    fun setGoogleMap(googleMap: GoogleMap) {
        map = googleMap
    }

    fun startTracking() {
        isTracking = true
        updateLocationTracking(isTracking)

    }

    fun stopTracking() {
        isTracking = false
        updateLocationTracking(isTracking)
    }


    private fun onLocationReceived(location: Location) {
        mPath.value?.addPathPoint(location)
        moveCameraToUser()
        addLatestPolyline()
        Timber.d("NEW LOCATION: ${location.latitude}, ${location.longitude}")
    }

    /* keep the camera focus on the path */
    private fun moveCameraToUser() {
        if (mPath.value!!.isEmpty()) {
            map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            mPath.value!!.last(),
                            MAP_ZOOM
                    )
            )
        }
    }
    /* add a new poly line */
    private fun addLatestPolyline() {
        if (mPath.value!!.hasAtLeastTowPoints()) {
            val preLastLatLng = mPath.value!!.getPreLastLatLng()
            val lastLatLng = mPath.value!!.getLastLatLng()
            val polyLineOptions = PolylineOptions()
                    .color(POLYLINE_COLOR)
                    .width(POLYLINE_WIDTH)
                    .add(preLastLatLng)
                    .add(lastLatLng)
            map?.addPolyline(polyLineOptions)
        }
    }

        /* redraw all polylines after rotation */
         fun addAllPolylines() {
            for (polyline in mPath.value!!.getPolylines()) {
                val polyLineOptions = PolylineOptions()
                        .color(POLYLINE_COLOR)
                        .width(POLYLINE_WIDTH)
                        .addAll(polyline)
                map?.addPolyline(polyLineOptions)
            }
        }

/*
        private fun zoomToSeeWholeTrack() {
            val bounds = LatLngBounds.builder()
            for (polyline in pathPoints)
                for (pos in polyline) {
                    bounds.include(pos)
                }
            map?.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(
                            bounds.build(),
                            binding.mapView.width,
                            binding.mapView.height,
                            (binding.mapView.height * 0.05F).toInt()
                    )
            )
        }
    */
    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermissions(app)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                        request,
                        locationCallback,
                        Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking) {
                result.locations.let { locations ->
                    for (location in locations) {
                        onLocationReceived(location)
                    }
                }
            }
        }
    }
}