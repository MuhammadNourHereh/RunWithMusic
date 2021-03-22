package com.nourtech.wordpress.runwithmusic.services.component.map

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.nourtech.wordpress.runwithmusic.others.Constants.FASTEST_LOCATION_INTERVAL
import com.nourtech.wordpress.runwithmusic.others.Constants.LOCATION_UPDATE_INTERVAL
import com.nourtech.wordpress.runwithmusic.others.TrackingUtility
import timber.log.Timber


class TrackingMap(val app: Context) {

    private val mPath = MutableLiveData<Path>()
    val path: LiveData<Path>
        get() = mPath

    private var isTracking = false


    private val fusedLocationProviderClient = FusedLocationProviderClient(app)

    fun startTracking() {
        isTracking = true
        updateLocationTracking(isTracking)
    }

    fun stopTracking() {
        isTracking = false
        updateLocationTracking(isTracking)
    }


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
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if (isTracking) {
                result?.locations?.let { locations ->
                    for (location in locations) {
                        mPath.value?.addPathPoint(location)
                        Timber.d("NEW LOCATION: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }
}