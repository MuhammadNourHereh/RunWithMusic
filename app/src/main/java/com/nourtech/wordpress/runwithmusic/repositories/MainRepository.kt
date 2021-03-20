package com.nourtech.wordpress.runtracker.repositories


import com.nourtech.wordpress.runtracker.db.RunDAO
import com.nourtech.wordpress.runtracker.db.RunEntity
import javax.inject.Inject

class MainRepository @Inject constructor(val runDao: RunDAO) {

    suspend fun insertRun(run: RunEntity) = runDao.insertRun(run)

    suspend fun deleteRun(run: RunEntity) = runDao.deleteRun(run)

    fun getAllRunsSortedByDate() = runDao.getAllRunsSortedByDate()

    fun getAllRunsSortedByDistance() = runDao.getAllRunsSortedByDistance()

    fun getAllRunsSortedByTimeInMillis() = runDao.getAllRunsSortedByTimeInMillis()

    fun getAllRunsSortedByAvgSpeed() = runDao.getAllRunsSortedByAvgSpeed()

    fun getAllRunsSortedByCaloriesBurned() = runDao.getAllRunsSortedByCaloriesBurned()

    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()

    fun getTotalDistance() = runDao.getTotalDistance()

    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()
}