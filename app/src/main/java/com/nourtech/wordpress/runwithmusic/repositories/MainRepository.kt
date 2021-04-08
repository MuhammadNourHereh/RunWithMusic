package com.nourtech.wordpress.runwithmusic.repositories

import com.nourtech.wordpress.runwithmusic.db.PlaylistEntity
import com.nourtech.wordpress.runwithmusic.db.PlaylistsDAO
import com.nourtech.wordpress.runwithmusic.db.RunDAO
import com.nourtech.wordpress.runwithmusic.db.RunEntity
import javax.inject.Inject


class MainRepository @Inject constructor(private val playlistsDAO: PlaylistsDAO,
                                         private val runDao: RunDAO) {

    fun getPlaylists() = playlistsDAO.getAllPlaylists()

    suspend fun insertPlaylists(playlistEntity: PlaylistEntity) {
        playlistsDAO.insertPlaylist(playlistEntity)
    }

    suspend fun deletePlaylists(playlistEntity: PlaylistEntity) {
        playlistsDAO.deletePlaylist(playlistEntity)
    }

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