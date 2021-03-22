package com.nourtech.wordpress.runwithmusic.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.nourtech.wordpress.runwithmusic.R
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
    private var isTracking = false

    @Inject
    lateinit var stopwatch: Stopwatch

    private var map: GoogleMap? = null

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

        // persist map data
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            map = it
            //addAllPolylines()
        }

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
        subscribeToService()

    }
    private fun toggleRun() {
        isTracking = if (isTracking) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
            binding.btnFinishRun.visibility = View.VISIBLE
            binding.btnToggleRun.text = getString(R.string.start)
            false
        }
        else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            binding.btnFinishRun.visibility = View.GONE
            binding.btnToggleRun.text = getString(R.string.pause)
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
    private fun subscribeToService() {
        TrackingService.isTracking.observe(viewLifecycleOwner) {
            isTracking = it
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}