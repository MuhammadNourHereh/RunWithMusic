package com.nourtech.wordpress.runwithmusic.others

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nourtech.wordpress.runwithmusic.R
import com.nourtech.wordpress.runwithmusic.others.Constants.NOTIFICATION_CHANNEL_ID
import com.nourtech.wordpress.runwithmusic.others.Constants.NOTIFICATION_CHANNEL_NAME
import com.nourtech.wordpress.runwithmusic.others.Constants.NOTIFICATION_ID
import timber.log.Timber

class TrackingNotification(val context: Context) {

    private var notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    private var builder = NotificationCompat.Builder(context, "")
        .setContentTitle("this is my first notification")
        .setSmallIcon(R.drawable.ic_run)
        .setChannelId(NOTIFICATION_CHANNEL_ID)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    fun updateNotification(time: Long) {
        val formattedTime = TrackingUtility.getFormattedStopWatchTime(time, false)
        val notification = builder
            .setContentText(formattedTime)
            .build()
        notificationManagerCompat.notify(NOTIFICATION_ID, notification)
        Timber.d("this is my notification")
    }

    fun cancelTheNotification() {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancel(NOTIFICATION_ID)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManagerCompat.createNotificationChannel(channel)
    }

}