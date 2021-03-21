package com.nourtech.wordpress.runwithmusic.services

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_PAUSE_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_STOP_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Stopwatch
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class TrackingService : LifecycleService(){


    @Inject
    lateinit var stopwatch: Stopwatch

    // receive the command from out source intent
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    Timber.d("started service")
                    stopwatch.startTimer()
                    subscribeToStopwatch()
                }

                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                    stopwatch.pauseTimer()

                }

                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                    stopwatch.reset()

                }
            }
        }
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

    private fun subscribeToStopwatch() {
        stopwatch.timeRunInMillis.observe(this) {
            Timber.d("the time in millis is :$it")
        }
        stopwatch.timeRunInSeconds.observe(this) {
            Timber.d("the time in seconds is :$it")
        }
    }

}
