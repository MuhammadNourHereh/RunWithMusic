package com.nourtech.wordpress.runwithmusic.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nourtech.wordpress.runwithmusic.databinding.FragmentMediaPlayerBinding
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SEEK_MUSIC
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SKIP_NEXT_SONG
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SKIP_PREVIOUS_SONG
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_START_PAUSE_MUSIC
import com.nourtech.wordpress.runwithmusic.others.TrackingUtility.getFormattedStopWatchTime
import com.nourtech.wordpress.runwithmusic.services.TrackingService
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX.Companion.curProgress
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX.Companion.curSongTime
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX.Companion.currentSongDuration
import com.nourtech.wordpress.runwithmusic.ui.viewmodels.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentMediaPlayerBinding
    private val viewModel: MusicViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMediaPlayerBinding.inflate(inflater, container, false)
        // necessary call for creating viewModel
        viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()

        binding.apply {
            if (MediaPlayerX.single) {
                MediaPlayerX.song?.let {
                    this.tvTitle.text = it.title
                }
            } else {
                MediaPlayerX.curPlayList.getCurrent()
            }
        }

        binding.apply {
            ivSkipNext.setOnClickListener {
                sendCommandToService(ACTION_SKIP_NEXT_SONG)
            }
            ivSkipPrevious.setOnClickListener {
                sendCommandToService(ACTION_SKIP_PREVIOUS_SONG)
            }
            ivPlay.setOnClickListener {
                sendCommandToService(ACTION_START_PAUSE_MUSIC)
            }
            ivRepeat.setOnClickListener {

            }
            binding.sbProgress.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        currentSongDuration.value?.let{
                            sendCommandToService(ACTION_SEEK_MUSIC, progress * it.toInt()/ 100)
                        }

                        seekBar?.progress = progress
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })
        }
    }

    private fun sendCommandToService(action: String) =
            Intent(requireContext(), TrackingService::class.java).also {
                it.action = action
                requireContext().startService(it)
            }

    private fun sendCommandToService(action: String, seekTo: Int) =
            Intent(requireContext(), TrackingService::class.java).also {
                it.action = action
                it.putExtra("SEEK_VALUE", seekTo )
                requireContext().startService(it)
            }

    private fun subscribeToObservers() {
        curProgress.observe(viewLifecycleOwner) {
            binding.sbProgress.progress = it
        }

        currentSongDuration.observe(viewLifecycleOwner) {
            binding.tvSongTime.text = getFormattedStopWatchTime(it)
        }

        curSongTime.observe(viewLifecycleOwner) {
            binding.tvSongTime.text = it
        }
    }

}