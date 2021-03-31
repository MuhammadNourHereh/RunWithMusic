package com.nourtech.wordpress.runwithmusic.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RunEntity::class, PlaylistEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Util::class)
abstract class DataBase : RoomDatabase() {

    abstract fun getRunDao(): RunDAO
    abstract fun getPlaylistsDAO(): PlaylistsDAO

}