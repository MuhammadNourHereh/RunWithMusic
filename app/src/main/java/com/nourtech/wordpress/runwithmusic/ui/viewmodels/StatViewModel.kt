package com.nourtech.wordpress.runtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.nourtech.wordpress.runtracker.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatViewModel @Inject constructor(val repo: MainRepository): ViewModel() {

    val totalTimeRun = repo.getTotalTimeInMillis()
    val totalDistance = repo.getTotalDistance()
    val totalCaloriesBurned = repo.getTotalCaloriesBurned()
    val totalAvgSpeed = repo.getTotalAvgSpeed()

    val runsSortedByDate = repo.getAllRunsSortedByDate()


}