package com.nourtech.wordpress.runwithmusic.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.nourtech.wordpress.runwithmusic.others.Constants
import com.nourtech.wordpress.runwithmusic.repositories.MainRepository
import com.nourtech.wordpress.runwithmusic.services.components.map.Path
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(val repo: MainRepository): ViewModel()  {

    var map: GoogleMap? = null
    private val mPath = MutableLiveData<Path>().apply {
        postValue(Path())
    }
    val path: LiveData<Path>
        get() = mPath

    fun addPoint(latLng: LatLng){
        mPath.apply {
            value?.addPathPoint(latLng)
            mPath.postValue(this.value)
        }
    }
    /* add a new poly line */
     fun addLatestPolyline() {
        if (path.value?.hasAtLeastTowPoints() == true) {
            val preLastLatLng = path.value!!.getPreLastLatLng()
            val lastLatLng = path.value!!.getLastLatLng()
            val polyLineOptions = PolylineOptions()
                    .color(Constants.POLYLINE_COLOR)
                    .width(Constants.POLYLINE_WIDTH)
                    .add(preLastLatLng)
                    .add(lastLatLng)
            map?.addPolyline(polyLineOptions)
        }
    }

    fun saveRun() {
        repo
    }
}