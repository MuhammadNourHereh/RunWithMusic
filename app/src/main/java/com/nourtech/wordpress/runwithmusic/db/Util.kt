package com.nourtech.wordpress.runwithmusic.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
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
}