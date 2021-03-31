package com.nourtech.wordpress.runwithmusic.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nourtech.wordpress.runwithmusic.others.Song

@Entity(tableName = "playlists_table")
data class PlaylistEntity(@ColumnInfo var songs: List<Song>){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
