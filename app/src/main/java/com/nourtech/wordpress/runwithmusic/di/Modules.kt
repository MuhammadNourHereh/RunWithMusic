package com.nourtech.wordpress.runwithmusic.di

import android.content.Context
import android.provider.MediaStore
import androidx.room.Room
import com.nourtech.wordpress.runwithmusic.db.RunDB
import com.nourtech.wordpress.runwithmusic.others.Constants.RUN_DB_NAME
import com.nourtech.wordpress.runwithmusic.others.Song
import com.nourtech.wordpress.runwithmusic.services.components.Stopwatch
import com.nourtech.wordpress.runwithmusic.services.components.map.TrackingMap
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context): RunDB =
        Room.databaseBuilder(app, RunDB::class.java, RUN_DB_NAME)
            .build()

    @Singleton
    @Provides
    fun provideDoa(db: RunDB) = db.getDao()

    @Singleton
    @Provides
    fun provideStopWatch() = Stopwatch()

    @Singleton
    @Provides
    fun provideTrackingMap(@ApplicationContext app: Context) = TrackingMap(app)

    @Singleton
    @Provides
    fun provideAudioMedia(@ApplicationContext app: Context): List<Song> {
        val songList = mutableListOf<Song>()
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val songCursor = app.contentResolver.query(songUri, null, null, null, null)
                ?: return songList

        if (songCursor.moveToFirst())
            do {
                songList.add(
                        Song(
                                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                        )
                )
            } while (songCursor.moveToNext())
        return songList
    }

}