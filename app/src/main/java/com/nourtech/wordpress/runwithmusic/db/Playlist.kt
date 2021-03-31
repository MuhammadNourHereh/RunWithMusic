package com.nourtech.wordpress.runwithmusic.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nourtech.wordpress.runwithmusic.others.Song
import timber.log.Timber
import java.io.Serializable


class Playlist: Serializable  {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null


    var songs: MutableList<Song> = mutableListOf()

    var cur = 0

    var state :Loop = Loop.NULL



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
        Timber.v("song  ${song.title} is added")
    }

    fun remove(song: Song) {
        // todo fix cur
        songs.remove(song)
    }

    fun isEmpty(): Boolean {
        return songs.isEmpty()
    }
    enum class Loop{
        ALL, CURRENT, NULL
    }
}

