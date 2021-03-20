package com.nourtech.wordpress.runwithmusic.di

import android.content.Context
import androidx.room.Room
import com.nourtech.wordpress.runwithmusic.db.RunDAO
import com.nourtech.wordpress.runwithmusic.db.RunDB
import com.nourtech.wordpress.runwithmusic.others.Constants.RUN_DB_NAME
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
    fun provideDoa(db: RunDB): RunDAO = db.getDao()

}