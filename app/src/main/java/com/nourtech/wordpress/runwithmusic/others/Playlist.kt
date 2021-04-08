package com.nourtech.wordpress.runwithmusic.others

import com.nourtech.wordpress.runwithmusic.db.PlaylistEntity
import timber.log.Timber
import java.io.Serializable


class Playlist(private var name: String) : Serializable {

    private var id: Int? = null
    private var songs: MutableList<Song> = mutableListOf()
    private var cur = 0


    constructor(id: Int, name: String, songs: List<Song>) : this(name) {
        this.songs = songs as MutableList<Song>
        this.id = id
    }

    constructor(name: String, songs: List<Song>) : this(name) {
        this.songs = songs as MutableList<Song>
    }


    fun getName() = name

    fun setName(name: String) {
        this.name = name
    }

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

    fun getCurrent(): Song? {
        if (songs.isEmpty())
            return null
        return songs[cur]
    }

    fun get(index: Int): Song {
        return songs[index]
    }

    fun getAll(): List<Song> = songs

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

    fun toPlaylistEntity(): PlaylistEntity = PlaylistEntity(id, name, songs)
    fun getCount() = songs.size
    fun complete() = songs.size == cur + 1
    fun setIndex(index: Int) {
        cur = index
    }


    companion object {
        fun toPlayLists(playlistEntities: List<PlaylistEntity>): List<Playlist> {
            val list = mutableListOf<Playlist>()
            for (playlistEntity in playlistEntities) {
                playlistEntity.id?.let {
                    list.add(Playlist(it, playlistEntity.name, playlistEntity.songs))
                } ?: list.add(Playlist(playlistEntity.name, playlistEntity.songs))

            }
            return list.toList()
        }
    }
}

