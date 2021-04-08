package com.nourtech.wordpress.runwithmusic.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nourtech.wordpress.runwithmusic.others.Song
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var playlistsDAO: PlaylistsDAO
    private lateinit var db: DataBase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, DataBase::class.java).build()
        playlistsDAO = db.getPlaylistsDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

}