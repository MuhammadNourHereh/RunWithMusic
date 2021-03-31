package com.nourtech.wordpress.runwithmusic.services

import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.nourtech.wordpress.runwithmusic.db.Playlist
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_NEXT_SONG
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_PAUSE_MUSIC
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_PAUSE_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_PREVIOUS_SONG
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_RESUME_MUSIC
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_START_MUSIC
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_STOP_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.CURRENT_PLAYLIST
import com.nourtech.wordpress.runwithmusic.others.Constants.CURRENT_SONG_PATH
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

    private val mediaPlayer = MediaPlayer()
    private var curPlayList = Playlist()

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
                ACTION_START_MUSIC -> {
                    it.getStringExtra(CURRENT_SONG_PATH)?.let { it1 -> setSource(it1) }
                    it.getSerializableExtra(CURRENT_PLAYLIST)?.let { p -> curPlayList = p as Playlist }
                }
                ACTION_RESUME_MUSIC -> {
                    playMusic()
                }
                ACTION_PAUSE_MUSIC -> {
                    pauseMusic()
                }
                ACTION_NEXT_SONG -> {
                    curPlayList.next()
                    setSource(curPlayList.getCurrent().path)
                    playMusic()
                }
                ACTION_PREVIOUS_SONG -> {
                    curPlayList.previous()
                    setSource(curPlayList.getCurrent().path)
                    playMusic()

                }
                else -> {

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
        isTracking.postValue(false)
        ////subscribeToStopwatch()
        /////subscribeToIsTracking()
        super.onCreate()
        startForeground(NOTIFICATION_ID, trackingNotification.getNotification())
        mediaPlayer.setOnCompletionListener {
            trackingNotification.updateAction(false)
            if (curPlayList.isEmpty())
                stopForeground(true)
            else
                when (curPlayList.state) {
                    Playlist.Loop.NULL -> {
                        stopForeground(true)
                    }
                    Playlist.Loop.ALL -> {
                        curPlayList.next()
                        setSource(curPlayList.getCurrent().path)
                        mediaPlayer.start()
                    }
                    Playlist.Loop.CURRENT -> {
                        mediaPlayer.start()
                    }
                }
        }
    }

    override fun onDestroy() {
        Timber.d("the service has been destroyed")
        super.onDestroy()
        mediaPlayer.release()
    }


    private fun setSource(src: String) {

        mediaPlayer.reset()
        mediaPlayer.apply {
            setDataSource(src)
            prepareAsync()
            setOnPreparedListener {
                playMusic()
                trackingNotification.updateAction(true)
            }
        }
    }

    private fun playMusic() {
        if (!mediaPlayer.isPlaying)
            mediaPlayer.start()
        trackingNotification.updateAction(true)

    }

    private fun pauseMusic() {
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
        trackingNotification.updateAction(false)
    }

}
