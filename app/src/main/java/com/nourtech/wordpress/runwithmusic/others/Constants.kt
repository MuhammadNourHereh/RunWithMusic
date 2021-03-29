package com.nourtech.wordpress.runwithmusic.others

import android.graphics.Color

object Constants {
    const val RUN_DB_NAME = "running_db"

    //stopwatch const
    const val TIMER_UPDATE_INTERVAL = 50L

    //service commands
    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"

    // service music commands
    const val ACTION_START_MUSIC = "ACTION_START_MUSIC"
    const val ACTION_PAUSE_MUSIC = "ACTION_PAUSE_MUSIC"
    const val CURRENT_SONG_PATH = "CURRENT_SONG_PATH"
    const val ACTION_RESUME_MUSIC = "ACTION_RESUME_MUSIC"

    //notification const
    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1

    //action
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    //request permission
    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    //location const
    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    //polyline const
    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 15f

}