package com.nourtech.wordpress.runwithmusic.services.components

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import com.nourtech.wordpress.runwithmusic.others.Playlist
import com.nourtech.wordpress.runwithmusic.others.Song
import kotlinx.coroutines.*

class MediaPlayerX : MediaPlayer() {

    private var job = Job()

    companion object {

        var curPlayList = MutableLiveData<Playlist>().also {
            it.postValue(Playlist("current_playlist"))
        }
        var state = MutableLiveData<Loop>().also {
            it.postValue(Loop.NULL)
        }
        var shuffle = MutableLiveData<Boolean>().also {
            it.postValue(false)
        }
        var curSongTitle = MutableLiveData<String>().also {
            it.postValue("Title")
        }
        var curSongArtist = MutableLiveData<String>().also {
            it.postValue("Artist")
        }
        var curIsPlaying = MutableLiveData<Boolean>().also {
            it.postValue(false)
        }
        var currentSongDuration = MutableLiveData<Long>().also {
            it.postValue(0)
        }
        var curProgress = MutableLiveData<Int>().also {
            it.postValue(0)
        }
        var curSongTime = MutableLiveData<Int>().also {
            it.postValue(0)
        }
    }

    enum class Loop {
        ALL, CURRENT, NULL
    }
    init {
        onCompleteListener()
    }

    fun setSong(song: Song) {
        curPlayList.postValue(Playlist(song.title, listOf(song)))
        setSource()
    }
    fun setPlaylist(playlist: Playlist) {
        curPlayList.postValue(playlist)
        setSource()
    }

    fun playPause() {
        try {
            if (isPlaying) {
                pause()
            } else {
                start()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        refresh()
    }
    fun skipNext() {
        curPlayList.value?.apply {
            this.next()
            curPlayList.postValue(this)
        }
        setSource()
        start()
    }
    fun skipPrevious() {
        curPlayList.value?.apply {
            this.previous()
            curPlayList.postValue(this)
        }
        setSource()
        start()
    }
    override fun seekTo(msec: Int) {
        super.seekTo(msec)
        curSongTime.postValue(currentPosition)
    }
    fun changeLoop() {
        when (state.value) {
            Loop.NULL -> {
                state.postValue(Loop.CURRENT)
            }
            Loop.CURRENT -> {
                state.postValue(Loop.ALL)
            }
            Loop.ALL -> {
                state.postValue(Loop.NULL)
            }
        }
    }
    fun shuffle() {
        shuffle.postValue(!shuffle.value!!)
        //TODO("not implemented yet")
    }

    private fun setSource() {
        val src = curPlayList.value?.getCurrent()
        reset()
        setDataSource(src!!.path)
        prepare()
        initJob()
        job.start()
        refresh()
    }


    fun refresh() {
        val src = curPlayList.value?.getCurrent()

        curSongTitle.postValue(src?.title)
        curSongArtist.postValue(src?.artist)
        curIsPlaying.postValue(isPlaying)
        currentSongDuration.postValue(duration.toLong())
        curProgress.postValue(((100f / duration) * currentPosition).toInt())
        curSongTime.postValue(currentPosition)
    }

    private fun initJob() {
        if (job.isActive)
            job.cancel()
        val scope = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                if (isPlaying) {
                    withContext(Dispatchers.Main) {
                        // update progress bar value
                        curProgress.postValue(((100f / duration) * currentPosition).toInt())

                        // update text
                        curSongTime.postValue(currentPosition)
                    }
                }
                delay(1000)
            }
        }
        job = Job(scope)
    }
    private fun onCompleteListener() {
        setOnCompletionListener {
            refresh()
            when (state.value) {
                Loop.NULL -> {

                }
                Loop.CURRENT -> {
                    start()
                }
                Loop.ALL -> {
                    skipNext()
                }
            }
        }
    }

}