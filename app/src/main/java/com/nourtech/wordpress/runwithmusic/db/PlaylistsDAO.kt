package com.nourtech.wordpress.runwithmusic.db

import androidx.room.*

@Dao
interface PlaylistsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_table ")
    fun getAllPlaylists(): List<PlaylistEntity>
}