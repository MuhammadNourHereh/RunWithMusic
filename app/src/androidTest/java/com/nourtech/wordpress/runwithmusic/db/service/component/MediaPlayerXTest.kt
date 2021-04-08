package com.nourtech.wordpress.runwithmusic.db.service.component

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.nourtech.wordpress.runwithmusic.others.Playlist
import com.nourtech.wordpress.runwithmusic.others.Song
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaPlayerXTest {

    lateinit var mediaPlayerX: MediaPlayerX

    @Before
    fun before() {
        mediaPlayerX = MediaPlayerX()
    }

    @After
    fun after() {
        mediaPlayerX.release()
    }

    @Test
    fun checkMediaPlayerInstanceCreated() {
        assertThat(mediaPlayerX).isNotNull()
    }

    @Test
    fun singleSongSetTest() {
        val song = Song("title", "artist", "path")
        mediaPlayerX.setSong(song)
        assertThat(MediaPlayerX.curPlayList.value!!.getAll()).containsExactly(song)
    }

    @Test
    fun playlistSetTest() {
        val list = listOf(
            Song("title 1", "artist 1", "path 1"),
            Song("title 2", "artist 2", "path 2"),
            Song("title 3", "artist 3", "path 3")
        )
        val playlist = Playlist("playlist", list)

        mediaPlayerX.setPlaylist(playlist)
        assertThat(MediaPlayerX.curPlayList.value!!.getAll()).containsExactlyElementsIn(list)
    }


    @Test
    fun singleSongSkipTest() {
        val song = Song("title", "artist", "path")
        mediaPlayerX.setSong(song)
        mediaPlayerX.skipNext()
        assertThat(MediaPlayerX.curPlayList.value!!.getCurrent()).isEqualTo(song)
    }



}