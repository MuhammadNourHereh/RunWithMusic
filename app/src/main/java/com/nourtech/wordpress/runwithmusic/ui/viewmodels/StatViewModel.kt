package com.nourtech.wordpress.runwithmusic.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.nourtech.wordpress.runwithmusic.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class StatViewModel @Inject constructor(repo: MainRepository): ViewModel() {

    val totalTimeRun = repo.getTotalTimeInMillis()
    val totalDistance = repo.getTotalDistance()
    val totalCaloriesBurned = repo.getTotalCaloriesBurned()
    val totalAvgSpeed = repo.getTotalAvgSpeed()
    val runsSortedByDate = repo.getAllRunsSortedByDate()

}