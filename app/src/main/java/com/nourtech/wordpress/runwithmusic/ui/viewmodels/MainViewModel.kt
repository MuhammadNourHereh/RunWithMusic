package com.nourtech.wordpress.runwithmusic.ui.viewmodels

import androidx.lifecycle.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.nourtech.wordpress.runwithmusic.db.RunEntity
import com.nourtech.wordpress.runwithmusic.others.Constants
import com.nourtech.wordpress.runwithmusic.others.SortType
import com.nourtech.wordpress.runwithmusic.repositories.MainRepository
import com.nourtech.wordpress.runwithmusic.services.components.map.Path
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repo: MainRepository): ViewModel()  {

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

    private val runsSortedByDate = repo.getAllRunsSortedByDate()
    private val runsSortedByDistance = repo.getAllRunsSortedByDistance()
    private val runsSortedByCaloriesBurned = repo.getAllRunsSortedByCaloriesBurned()
    private val runsSortedByTimeInMillis = repo.getAllRunsSortedByTimeInMillis()
    private val runsSortedByAvgSpeed = repo.getAllRunsSortedByAvgSpeed()

    val runs = MediatorLiveData<List<RunEntity>>()

    var sortType = SortType.DATE

    init {
        runs.addSource(runsSortedByDate) { result ->
            if(sortType == SortType.DATE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByAvgSpeed) { result ->
            if(sortType == SortType.AVG_SPEED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByCaloriesBurned) { result ->
            if(sortType == SortType.CALORIES_BURNED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByDistance) { result ->
            if(sortType == SortType.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMillis) { result ->
            if(sortType == SortType.RUNNING_TIME) {
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType) = when(sortType) {
        SortType.DATE -> runsSortedByDate.value?.let { runs.value = it }
        SortType.RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runs.value = it }
        SortType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
        SortType.DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
        SortType.CALORIES_BURNED -> runsSortedByCaloriesBurned.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }

    fun insertRun(run: RunEntity) = viewModelScope.launch {
        repo.insertRun(run)
    }
}