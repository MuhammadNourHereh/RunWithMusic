package com.nourtech.wordpress.runwithmusic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nourtech.wordpress.runwithmusic.adapters.MusicListAdapter
import com.nourtech.wordpress.runwithmusic.databinding.FragmentMusicBinding
import com.nourtech.wordpress.runwithmusic.others.Song

class MusicFragment: Fragment() {

    private lateinit var binding: FragmentMusicBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set up recycler view
        binding.recyclerviewMusic.apply {
            adapter = MusicListAdapter(listOf(
                    Song("title 1", "artist 1", ""),
                    Song("title 2", "artist 2", ""),
                    Song("title 3", "artist 3", "")
            ))
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}