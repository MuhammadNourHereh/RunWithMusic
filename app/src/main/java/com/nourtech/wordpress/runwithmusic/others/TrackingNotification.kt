package com.nourtech.wordpress.runwithmusic.others

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nourtech.wordpress.runwithmusic.R
import dagger.hilt.android.qualifiers.ApplicationContext

class TrackingNotification(val context: Context) {

     fun buildNotification() {

        val notificationManagerCompat = NotificationManagerCompat.from(context)

        val builder = NotificationCompat.Builder(context, "")
            .setContentTitle("this is my first notification")
            .setSmallIcon(R.drawable.ic_run)
        val notification = builder.build()

        notificationManagerCompat.notify(1, notification)

    }

    fun cancelTheNotification() {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancel(1)
    }

}