package com.nourtech.wordpress.runwithmusic.ui.fragments

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nourtech.wordpress.runwithmusic.R
import com.nourtech.wordpress.runwithmusic.adapters.MusicListAdapter
import com.nourtech.wordpress.runwithmusic.databinding.FragmentMusicBinding
import com.nourtech.wordpress.runwithmusic.others.Song
import com.nourtech.wordpress.runwithmusic.ui.viewmodels.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicFragment: Fragment() {

    private lateinit var binding: FragmentMusicBinding
    private val viewModel: MusicViewModel by viewModels()
    private var playList = false

    @Inject
    lateinit var songList: List<Song>


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentMusicBinding.inflate(inflater, container, false)

        // to access storage
        getPermissions()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set up recycler view

        binding.recyclerviewMusic.apply {
            adapter = MusicListAdapter(songList)
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.icPlaylist.setOnClickListener {
            playList = if (playList) {
                (binding.recyclerviewMusic.adapter as MusicListAdapter).switchToAllList()
                binding.icPlaylist.setImageResource(R.drawable.ic_queue_music_48px)
                false
            } else {
                (binding.recyclerviewMusic.adapter as MusicListAdapter).switchToPlayList()
                binding.icPlaylist.setImageResource(R.drawable.ic_playlist_add_48px)
                true
            }
        }
    }

    private fun getPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return
        if (ActivityCompat.checkSelfPermission
                (requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    0
            )
            return
        }
    }
}