package com.nourtech.wordpress.runwithmusic.services.component

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nourtech.wordpress.runwithmusic.R
import com.nourtech.wordpress.runwithmusic.others.Constants
import com.nourtech.wordpress.runwithmusic.others.Constants.NOTIFICATION_CHANNEL_ID
import com.nourtech.wordpress.runwithmusic.others.Constants.NOTIFICATION_CHANNEL_NAME
import com.nourtech.wordpress.runwithmusic.others.Constants.NOTIFICATION_ID
import com.nourtech.wordpress.runwithmusic.others.TrackingUtility
import com.nourtech.wordpress.runwithmusic.ui.MainActivity
import timber.log.Timber

class TrackingNotification(val context: Context) {

    private var notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    private var builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
        .setContentTitle("Running App")
        .setContentIntent(PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).also {
                it.action = Constants.ACTION_SHOW_TRACKING_FRAGMENT
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        ))

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
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

    fun updateNotification(time: Long) {
        val formattedTime = TrackingUtility.getFormattedStopWatchTime(time * 1000L, false)
        val notification = builder
            .setContentText(formattedTime)
            .build()
        notificationManagerCompat.notify(NOTIFICATION_ID, notification)
        Timber.d("this is my notification: $formattedTime")
    }

    fun cancelTheNotification() {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancel(NOTIFICATION_ID)
    }

    fun getNotification(): Notification {
        return builder.build()
    }

}