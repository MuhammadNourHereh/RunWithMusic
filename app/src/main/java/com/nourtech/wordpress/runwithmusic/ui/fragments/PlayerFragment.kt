package com.nourtech.wordpress.runwithmusic.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nourtech.wordpress.runwithmusic.R
import com.nourtech.wordpress.runwithmusic.databinding.FragmentMediaPlayerBinding
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_CHANGE_REPEAT_STATE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_REFRESH_MEDIA
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SEEK_MEDIA
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SKIP_NEXT_MEDIA
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SKIP_PREVIOUS_MEDIA
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_START_PAUSE_MEDIA
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_TOGGLE_SHUFFLE
import com.nourtech.wordpress.runwithmusic.others.TrackingUtility.getFormattedStopWatchTime
import com.nourtech.wordpress.runwithmusic.services.TrackingService
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX.Companion.curIsPlaying
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX.Companion.curProgress
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX.Companion.curSongArtist
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX.Companion.curSongTime
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX.Companion.curSongTitle
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX.Companion.currentSongDuration
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX.Companion.shuffle
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX.Companion.state
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
        setOnClickListeners()

        sendCommandToService(ACTION_REFRESH_MEDIA)
    }

    private fun sendCommandToService(action: String) =
            Intent(requireContext(), TrackingService::class.java).also {
                it.action = action
                requireContext().startService(it)
            }

    private fun sendCommandToService(action: String, seekTo: Int) =
            Intent(requireContext(), TrackingService::class.java).also {
                it.action = action
                it.putExtra(ACTION_SEEK_MEDIA, seekTo)
                requireContext().startService(it)
            }

    private fun setOnClickListeners() {
        binding.apply {
            ivSkipNext.setOnClickListener {
                sendCommandToService(ACTION_SKIP_NEXT_MEDIA)
            }
            ivSkipPrevious.setOnClickListener {
                sendCommandToService(ACTION_SKIP_PREVIOUS_MEDIA)
            }
            ivPlay.setOnClickListener {
                sendCommandToService(ACTION_START_PAUSE_MEDIA)
            }
            ivRepeat.setOnClickListener {
                sendCommandToService(ACTION_CHANGE_REPEAT_STATE)
            }
            ivShuffle.setOnClickListener {
                sendCommandToService(ACTION_TOGGLE_SHUFFLE)
            }
            binding.sbProgress.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        currentSongDuration.value?.let {
                            sendCommandToService(ACTION_SEEK_MEDIA, (progress * it / 100).toInt())
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

    private fun subscribeToObservers() {
        curSongTitle.observe(viewLifecycleOwner) {
            binding.tvTitle.text = it
        }
        curSongArtist.observe(viewLifecycleOwner) {
            binding.tvArtist.text = it
        }
        curIsPlaying.observe(viewLifecycleOwner) {
            if (it) {
                binding.ivPlay.setImageResource(R.drawable.ic_pause_white_48dp)
            } else {
                binding.ivPlay.setImageResource(R.drawable.ic_play_white_48dp)
            }
        }
        curProgress.observe(viewLifecycleOwner) {
            binding.sbProgress.progress = it
        }

        currentSongDuration.observe(viewLifecycleOwner) {
            binding.tvSongTime.text = getFormattedStopWatchTime(it)
        }

        curSongTime.observe(viewLifecycleOwner) {
            binding.tvRunTime.text = getFormattedStopWatchTime(it.toLong())
        }
        shuffle.observe(viewLifecycleOwner) {
            if (it) {
                binding.ivShuffle.setImageResource(R.drawable.ic_shuffle_on_white_48dp)
            } else {
                binding.ivShuffle.setImageResource(R.drawable.ic_shuffle_white_48dp)
            }
        }
        state.observe(viewLifecycleOwner) {
            when (it!!) {
                MediaPlayerX.Loop.NULL -> {
                    binding.ivRepeat.setImageResource(R.drawable.ic_repeat_white_48dp)
                }
                MediaPlayerX.Loop.CURRENT -> {
                    binding.ivRepeat.setImageResource(R.drawable.ic_repeat_one_on_white_48dp)
                }
                MediaPlayerX.Loop.ALL -> {
                    binding.ivRepeat.setImageResource(R.drawable.ic_repeat_on_white_48dp)
                }
            }
        }
    }

}