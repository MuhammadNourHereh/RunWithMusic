package com.nourtech.wordpress.runwithmusic.repositories

import com.nourtech.wordpress.runwithmusic.db.PlaylistEntity
import com.nourtech.wordpress.runwithmusic.db.PlaylistsDAO
import javax.inject.Inject


class MainRepository @Inject constructor(private val playlistsDAO: PlaylistsDAO) {

    fun getPlaylists() = playlistsDAO.getAllPlaylists()

    suspend fun insertPlaylists(playlistEntity: PlaylistEntity) {
        playlistsDAO.insertPlaylist(playlistEntity)
    }

    suspend fun deletePlaylists(playlistEntity: PlaylistEntity) {
        playlistsDAO.deletePlaylist(playlistEntity)
    }


}