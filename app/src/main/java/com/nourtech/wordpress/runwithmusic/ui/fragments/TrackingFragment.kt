package com.nourtech.wordpress.runwithmusic.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nourtech.wordpress.runwithmusic.databinding.FragmentTrackingBinding
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_PAUSE_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_STOP_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Stopwatch
import com.nourtech.wordpress.runwithmusic.others.TrackingUtility
import com.nourtech.wordpress.runwithmusic.services.TrackingService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TrackingFragment : Fragment() {
    private lateinit var binding: FragmentTrackingBinding
    private var isTimerRunning = false

    @Inject
    lateinit var stopwatch: Stopwatch

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set on click listeners
        binding.apply {
            btnToggleRun.setOnClickListener {
                toggleRun()
            }
            btnFinishRun.setOnClickListener {
                stopRun()
            }
        }

        // subscribe to stopwatch
        subscribeToStopWatch()

    }
    private fun toggleRun() {
        isTimerRunning = if (isTimerRunning) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
            binding.btnFinishRun.visibility = View.VISIBLE
            false
        }
        else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            binding.btnFinishRun.visibility = View.GONE
            true
        }
    }
    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        //findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }
    private fun sendCommandToService(action: String) =
            Intent(requireContext(), TrackingService::class.java).also {
                it.action = action
                requireContext().startService(it)
            }

    private fun subscribeToStopWatch() {
        stopwatch.timeRunInMillis.observe(viewLifecycleOwner) {
            binding.tvTimer.text =
                    TrackingUtility.getFormattedStopWatchTime(it, true)
        }
    }
}