package com.nourtech.wordpress.runwithmusic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nourtech.wordpress.runwithmusic.adapters.PlaylistAdapter
import com.nourtech.wordpress.runwithmusic.databinding.FragmentPlaylistBinding
import com.nourtech.wordpress.runwithmusic.others.Constants.EXTRA_CHOSEN_PLAYLIST
import com.nourtech.wordpress.runwithmusic.ui.viewmodels.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PlayListFragment: Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel: MusicViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        // necessary call for creating viewModel
        viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSongs.apply {
            layoutManager = LinearLayoutManager(requireContext())

            GlobalScope.launch(Dispatchers.IO) {
                val list = viewModel.getPlaylists()

                withContext(Dispatchers.Main) {
                    adapter = PlaylistAdapter(list[arguments?.getInt(EXTRA_CHOSEN_PLAYLIST)!!],this@PlayListFragment)
                }
            }
        }
    }
}