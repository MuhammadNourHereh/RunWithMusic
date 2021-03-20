package com.nourtech.wordpress.runtracker.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nourtech.wordpress.runtracker.db.RunEntity
import com.nourtech.wordpress.runtracker.others.SortType
import com.nourtech.wordpress.runtracker.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: MainRepository): ViewModel()  {

    fun insertRun(runEntity: RunEntity) = viewModelScope.launch{
        repo.insertRun(runEntity)
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
}