package com.nourtech.wordpress.runwithmusic.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nourtech.wordpress.runwithmusic.db.PlaylistEntity
import com.nourtech.wordpress.runwithmusic.others.Playlist
import com.nourtech.wordpress.runwithmusic.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(private val repo: MainRepository) : ViewModel() {

    fun insertPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            repo.insertPlaylists(playlist.toPlaylistEntity())
        }
    }

    fun getPlaylists(): List<Playlist> = Playlist.toPlayLists(repo.getPlaylists())


    fun deletePlaylists(playlist: Playlist) {
        viewModelScope.launch {
            repo.deletePlaylists(playlist.toPlaylistEntity())
        }
    }
}
