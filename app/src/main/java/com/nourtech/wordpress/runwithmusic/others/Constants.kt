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
    const val ACTION_START_PAUSE_MEDIA = "ACTION_START_MEDIA"
    const val ACTION_PAUSE_MEDIA = "ACTION_PAUSE_MEDIA"
    const val ACTION_RESUME_MEDIA = "ACTION_RESUME_MEDIA"
    const val ACTION_SKIP_PREVIOUS_MEDIA = "ACTION_PREVIOUS_MEDIA"
    const val ACTION_SKIP_NEXT_MEDIA = "ACTION_NEXT_MEDIA"
    const val ACTION_SEEK_MEDIA = "ACTION_SEEK_MEDIA"
    const val ACTION_REFRESH_MEDIA = "ACTION_REFRESH_MEDIA"
    const val ACTION_CHANGE_REPEAT_STATE = "ACTION_CHANGE_REPEAT_STATE"
    const val ACTION_SET_SONG = "ACTION_SET_SONG"
    const val ACTION_SET_PLAYLIST = "ACTION_SET_PLAYLIST"
    const val ACTION_TOGGLE_SHUFFLE = "ACTION_TOGGLE_SHUFFLE"
    const val EXTRA_CURRENT_SONG = "EXTRA_CURRENT_SONG"
    const val EXTRA_CURRENT_PLAYLIST = "EXTRA_CURRENT_PLAYLIST"

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
    const val MAP_ZOOM = 18f

    //bundle
    const val EXTRA_CHOSEN_PLAYLIST = "EXTRA_CHOSEN_PLAYLIST"

    //shared preferences keys
    const val SHARED_PREFERENCES_NAME = "sharedPref"
    const val KEY_FIRST_TIME_TOGGLE = "KEY_FIRST_TIME_TOGGLE"
    const val KEY_NAME = "KEY_NAME"
    const val KEY_WEIGHT = "KEY_WEIGHT"


}