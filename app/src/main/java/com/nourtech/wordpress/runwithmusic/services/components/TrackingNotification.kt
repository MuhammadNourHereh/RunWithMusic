package com.nourtech.wordpress.runwithmusic.services.components

import android.annotation.SuppressLint
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
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_NEXT_SONG
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_PAUSE_MUSIC
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_PREVIOUS_SONG
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_RESUME_MUSIC
import com.nourtech.wordpress.runwithmusic.others.Constants.NOTIFICATION_CHANNEL_ID
import com.nourtech.wordpress.runwithmusic.others.Constants.NOTIFICATION_CHANNEL_NAME
import com.nourtech.wordpress.runwithmusic.others.Constants.NOTIFICATION_ID
import com.nourtech.wordpress.runwithmusic.others.TrackingUtility
import com.nourtech.wordpress.runwithmusic.services.TrackingService
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
        notificationManagerCompat.cancel(NOTIFICATION_ID)
    }

    fun getNotification(): Notification {
        return builder.build()
    }

    @SuppressLint("RestrictedApi")
    fun updateAction(musicOn: Boolean) {

        builder.mActions.clear()
        val icon = if (musicOn) R.drawable.ic_pause_black_24dp else R.drawable.ic_play
        val action = if (musicOn) ACTION_PAUSE_MUSIC else ACTION_RESUME_MUSIC
        val requestCode = if (musicOn) 1 else 2

        val notification = builder.addAction(R.drawable.ic_skip_previous_white_24dp,
                "",
                PendingIntent.getService(context, 3,
                        Intent(context, TrackingService::class.java).also {
                            it.action = ACTION_PREVIOUS_SONG
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                )).addAction(icon,
                "",
                PendingIntent.getService(context, requestCode,
                        Intent(context, TrackingService::class.java).also {
                            it.action = action
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                )).addAction(R.drawable.ic_skip_next_white_24dp,
                "",
                PendingIntent.getService(context, 4,
                        Intent(context, TrackingService::class.java).also {
                            it.action = ACTION_NEXT_SONG
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                )).build()
        notificationManagerCompat.notify(NOTIFICATION_ID, notification)

    }

}