package com.nourtech.wordpress.runwithmusic.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nourtech.wordpress.runwithmusic.db.Playlist
import com.nourtech.wordpress.runwithmusic.db.PlaylistEntity
import com.nourtech.wordpress.runwithmusic.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(private val repo: MainRepository) : ViewModel() {

    fun insertPlaylist(playlistEntity: PlaylistEntity) {
        viewModelScope.launch {
            repo.insertPlaylists(playlistEntity)
        }
    }

    fun getPlaylists(): LiveData<List<PlaylistEntity>> {
        return repo.getPlaylists()
    }

    fun deletePlaylists(playlistEntity: PlaylistEntity) {
        viewModelScope.launch {
            repo.deletePlaylists(playlistEntity)
        }
    }
}
