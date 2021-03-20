package com.nourtech.wordpress.runwithmusic.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RunEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Util::class)
abstract class RunDB : RoomDatabase() {
    abstract fun getDao(): RunDAO
}