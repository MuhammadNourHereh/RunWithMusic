package com.nourtech.wordpress.runwithmusic.services

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_CHANGE_REPEAT_STATE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_PAUSE_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SEEK_MEDIA
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SET_PLAYLIST
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SET_SONG
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SKIP_NEXT_MEDIA
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SKIP_PREVIOUS_MEDIA
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_START_PAUSE_MEDIA
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_STOP_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_TOGGLE_SHUFFLE
import com.nourtech.wordpress.runwithmusic.others.Constants.NOTIFICATION_ID
import com.nourtech.wordpress.runwithmusic.others.Constants.EXTRA_CURRENT_PLAYLIST
import com.nourtech.wordpress.runwithmusic.others.Constants.EXTRA_CURRENT_SONG
import com.nourtech.wordpress.runwithmusic.others.Playlist
import com.nourtech.wordpress.runwithmusic.others.Song
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX
import com.nourtech.wordpress.runwithmusic.services.components.Stopwatch
import com.nourtech.wordpress.runwithmusic.services.components.TrackingNotification
import com.nourtech.wordpress.runwithmusic.services.components.map.LocationProvider
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
    lateinit var locationProvider: LocationProvider

    private val mediaPlayerX = MediaPlayerX()

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
                // media player commands
                ACTION_SET_SONG -> {
                    val song = it.extras?.getSerializable(EXTRA_CURRENT_SONG) as Song
                    mediaPlayerX.setSong(song)
                }
                ACTION_SET_PLAYLIST -> {
                    val playlist = it.extras?.getSerializable(EXTRA_CURRENT_PLAYLIST) as Playlist
                    mediaPlayerX.setPlaylist(playlist)
                }
                ACTION_START_PAUSE_MEDIA -> {
                    mediaPlayerX.playPause()
                }
                ACTION_SKIP_NEXT_MEDIA -> {
                    mediaPlayerX.skipNext()
                }
                ACTION_SKIP_PREVIOUS_MEDIA -> {
                    mediaPlayerX.skipPrevious()
                }
                ACTION_SEEK_MEDIA -> {
                    val value = it.extras?.getInt(ACTION_SEEK_MEDIA)
                    mediaPlayerX.seekTo(value!!)
                }
                ACTION_CHANGE_REPEAT_STATE -> {
                    mediaPlayerX.changeLoop()
                }
                ACTION_TOGGLE_SHUFFLE -> {
                    mediaPlayerX.shuffle()
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
            if (isTracking.value == true)
                Timber.d("the time in seconds is :$it")
            trackingNotification.updateNotification(it)
        }
    }

    private fun subscribeToIsTracking() {
        isTracking.observe(this) {
            locationProvider.toggleTracking(it)
        }
    }

    // this service don't provide binding
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("the service has been created")
        isTracking.postValue(false)
        stopwatch // for initialize the property
        subscribeToStopwatch()
        subscribeToIsTracking()
        startForeground(NOTIFICATION_ID, trackingNotification.getNotification())

    }

    override fun onDestroy() {
        Timber.d("the service has been destroyed")
        mediaPlayerX.release()
        super.onDestroy()

    }

}
