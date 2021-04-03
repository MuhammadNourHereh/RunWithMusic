package com.nourtech.wordpress.runwithmusic.services.components

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import com.nourtech.wordpress.runwithmusic.others.Playlist
import com.nourtech.wordpress.runwithmusic.others.Song
import com.nourtech.wordpress.runwithmusic.others.TrackingUtility.getFormattedStopWatchTime
import kotlinx.coroutines.*
import timber.log.Timber

class MediaPlayerX : MediaPlayer() {

    private var curPath = ""
    private var job = Job()

    companion object {
        var curPlayList = Playlist("current_playlist")
        var state: Loop = Loop.NULL

        var song: Song? = null
        var single = true
        var repeat = false

        var currentSongDuration = MutableLiveData<Long>().also {
            it.postValue(0)
        }
        var curProgress = MutableLiveData<Int>().also {
            it.postValue(0)
        }
        var curSongTime = MutableLiveData<String>().also {
            it.postValue("00:00")
        }
    }

    enum class Loop {
        ALL, CURRENT, NULL
    }

    init {
        setOnCompletionListener {

            if (single) {
                isLooping = repeat

            } else
                if (!curPlayList.isEmpty())
                    when (state) {
                        Loop.NULL -> {

                        }
                        Loop.ALL -> {
                            curPlayList.next()
                            setSource()
                            start()
                        }
                        Loop.CURRENT -> {
                            start()
                        }
                    }
        }

    }

    private fun setSource() {

        job.cancel()
        reset()

        val src: String = if (single) song!!.path else curPlayList.getCurrent()?.path ?: return
        curPath = src

        setDataSource(src)
        currentSongDuration.postValue(duration.toLong())
        Timber.d("duration :$duration")
        initJob()
        prepareAsync()
        setOnPreparedListener {
            playMusic()
            job.start()

        }
    }

    fun playPause() {

        if (setSourceIfNeeded()) {
            setSource()
            Timber.d("source changed")
        } else {
            if (isPlaying)
                pause()
            else start()
        }
    }

    private fun playMusic() {
        if (!isPlaying)
            start()
    }

    fun skipNext() {
        if (!curPlayList.isEmpty()) {
            curPlayList.next()
            setSource()
        }
    }

    fun skipPrevious() {
        if (!curPlayList.isEmpty()) {
            curPlayList.previous()
            setSource()
        }
    }

    private fun setSourceIfNeeded(): Boolean {
        val path = if (single) {
            song?.path ?: ""
        } else {
            curPlayList.getCurrent()?.path ?: ""
        }
        return curPath != path || curPath == ""
    }

    private fun initJob() {
        val scope = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                if (isPlaying) {
                    withContext(Dispatchers.Main) {

                        // update progress bar value
                        curProgress.postValue(((100f / duration) * currentPosition).toInt())

                        // update text
                        curSongTime.postValue(getFormattedStopWatchTime(currentPosition.toLong(), false))

                    }
                    delay(1000)
                }
            }
        }
        job = Job(scope)

    }

}