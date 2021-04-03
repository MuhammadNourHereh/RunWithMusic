package com.nourtech.wordpress.runwithmusic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nourtech.wordpress.runwithmusic.adapters.PlaylistsAdapter
import com.nourtech.wordpress.runwithmusic.databinding.FragmentPlaylistsBinding
import com.nourtech.wordpress.runwithmusic.dialogs.Dialogs
import com.nourtech.wordpress.runwithmusic.ui.viewmodels.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel: MusicViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlaylists.layoutManager = LinearLayoutManager(requireContext())
        updateAdapter()

        binding.icPlaylist.setOnClickListener {
            Dialogs.newPlaylistDialog(requireContext(),viewModel).show()
        }
    }
    private fun updateAdapter() {
        binding.rvPlaylists.apply {
            layoutManager = LinearLayoutManager(requireContext())

            GlobalScope.launch(Dispatchers.IO) {
                val list = viewModel.getPlaylists()

                withContext(Dispatchers.Main) {
                    adapter = PlaylistsAdapter(list.toMutableList(), this@PlaylistsFragment, viewModel)
                }
            }
        }
    }

}