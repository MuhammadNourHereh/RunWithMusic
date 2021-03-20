package com.nourtech.wordpress.runwithmusic.services

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class TrackingService : LifecycleService(){


    // receive the command from out source intent
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    // this service don't provide binding
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onCreate() {
        Timber.d("the service has been created")
        super.onCreate()
    }

    override fun onDestroy() {
        Timber.d("the service has been destroyed")
        super.onDestroy()
    }
}
