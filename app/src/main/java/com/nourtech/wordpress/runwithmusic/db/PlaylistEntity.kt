package com.nourtech.wordpress.runwithmusic.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nourtech.wordpress.runwithmusic.others.Song

@Entity(tableName = "playlists_table")
data class PlaylistEntity(@PrimaryKey(autoGenerate = true) var id: Int? = null,
                          @ColumnInfo val name: String,
                          @ColumnInfo val songs: List<Song>)


