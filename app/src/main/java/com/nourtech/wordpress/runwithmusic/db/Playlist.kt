package com.nourtech.wordpress.runwithmusic.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nourtech.wordpress.runwithmusic.others.Song

@Entity(tableName = "running_table")
data class Playlist(var songs: MutableList<Song>) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    private var cur = 0

    fun next() {
        if (songs.isEmpty())
            return
        if (cur < songs.size - 1)
            cur++
        else cur = 0
    }

    fun previous() {
        if (songs.isEmpty())
            return
        if (cur == 0)
            cur = songs.size - 1
        else
            cur--
    }

    fun getCurrent(): Song {
        return songs[cur]
    }

    fun get (index: Int): Song {
        return songs[index]
    }


    fun add(song: Song) {
        songs.add(song)
    }

    fun remove(song: Song) {
        // todo fix cur
        songs.remove(song)
    }
}
