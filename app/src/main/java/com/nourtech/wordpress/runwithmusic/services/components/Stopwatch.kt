package com.nourtech.wordpress.runwithmusic.services.components

import androidx.lifecycle.MutableLiveData
import com.nourtech.wordpress.runwithmusic.others.Constants.TIMER_UPDATE_INTERVAL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Stopwatch {

    private var isTimerEnabled = false
    // time stopwatch started
    private var timeStarted = 0L

    // store last lab time
    private var currentLabTime = 0L

    // store overall time run without last lab
    // real run time = labTime + timeRun
    private var previousLabsTime = 0L

    // use to update time every second
    private var lastSecondTimeStamp = 0L

    val timeRunInMillis = MutableLiveData<Long>()
    val timeRunInSeconds = MutableLiveData<Long>()

    init {
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
    }

    fun startTimer() {
        if (!isTimerEnabled) {
            isTimerEnabled = true
            runTimer()
        }
    }

    fun pauseTimer() {
        isTimerEnabled = false
    }

    fun reset() {
        isTimerEnabled = false
        timeStarted = 0L
        currentLabTime = 0L
        previousLabsTime = 0L
        lastSecondTimeStamp = 0L
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
    }

    private fun runTimer() {
        CoroutineScope(Dispatchers.Main).launch {
            timeStarted = System.currentTimeMillis()

            while (isTimerEnabled) {

                // deference time between now and time started
                currentLabTime = System.currentTimeMillis() - timeStarted

                // post value lapTime
                timeRunInMillis.postValue(previousLabsTime + currentLabTime)

                if (currentLabTime >= lastSecondTimeStamp + 1000L) {
                    timeRunInSeconds.postValue((previousLabsTime + currentLabTime) / 1000)
                    lastSecondTimeStamp += 1000L
                }

                delay(TIMER_UPDATE_INTERVAL)
            }
            previousLabsTime += currentLabTime
            lastSecondTimeStamp %= 1000L
        }
    }

}