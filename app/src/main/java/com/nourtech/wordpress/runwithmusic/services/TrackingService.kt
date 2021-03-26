package com.nourtech.wordpress.runwithmusic.services

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_PAUSE_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_STOP_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.NOTIFICATION_ID
import com.nourtech.wordpress.runwithmusic.services.components.Stopwatch
import com.nourtech.wordpress.runwithmusic.services.components.TrackingNotification
import com.nourtech.wordpress.runwithmusic.services.components.map.TrackingMap
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class TrackingService : LifecycleService(){


    @Inject
    lateinit var stopwatch: Stopwatch

    @Inject
    lateinit var trackingNotification: TrackingNotification

    @Inject
    lateinit var trackingMap: TrackingMap

    companion object {
        var isTracking = MutableLiveData<Boolean>().also {
            it.postValue(false)
        }
    }

    // receive the command from out source intent
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    onStart()
                }

                ACTION_PAUSE_SERVICE -> {
                    onPause()
                }

                ACTION_STOP_SERVICE -> {
                    onStop()
                }
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun onStart() {
        Timber.d("started service")
        isTracking.postValue(true)
        stopwatch.startTimer()
        startForeground(NOTIFICATION_ID, trackingNotification.getNotification())

    }
    private fun onPause() {
        Timber.d("Paused service")
        isTracking.postValue(false)
        stopwatch.pauseTimer()
    }
    private fun onStop() {
        Timber.d("Stopped service")
        isTracking.postValue(false)
        stopwatch.reset()
        stopForeground(true)
        stopSelf()
    }

    private fun subscribeToStopwatch() {
        stopwatch.timeRunInSeconds.observe(this) {
            if (isTracking.value!!)
                Timber.d("the time in seconds is :$it")
            trackingNotification.updateNotification(it)
        }
    }

    private fun subscribeToIsTracking() {
        isTracking.observe(this) {
            trackingMap.toggleTracking(it)
        }
    }

    // this service don't provide binding
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onCreate() {
        Timber.d("the service has been created")
        subscribeToStopwatch()
        subscribeToIsTracking()
        super.onCreate()
    }

    override fun onDestroy() {
        Timber.d("the service has been destroyed")
        super.onDestroy()
    }


}
