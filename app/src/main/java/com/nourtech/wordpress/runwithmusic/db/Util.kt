package com.nourtech.wordpress.runwithmusic.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nourtech.wordpress.runwithmusic.others.Song
import java.io.ByteArrayOutputStream

class Util {
    @TypeConverter
    fun convertToBitmap(byte: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byte, 0, byte.size)
    }

    @TypeConverter
    fun convertFormBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun convertFormSongs(songs: List<Song>): String = Gson().toJson(songs)


    @TypeConverter
    fun convertToSongs(json: String): List<Song> {
        val type = object :TypeToken<List<Song>>() {}.type
        return Gson().fromJson(json, type)
    }



}