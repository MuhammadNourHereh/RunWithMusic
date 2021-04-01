package com.nourtech.wordpress.runwithmusic.ui.fragments

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.nourtech.wordpress.runwithmusic.R
import com.nourtech.wordpress.runwithmusic.databinding.FragmentTrackingBinding
import com.nourtech.wordpress.runwithmusic.others.Constants
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_PAUSE_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_STOP_SERVICE
import com.nourtech.wordpress.runwithmusic.others.TrackingUtility
import com.nourtech.wordpress.runwithmusic.services.TrackingService
import com.nourtech.wordpress.runwithmusic.services.components.Stopwatch
import com.nourtech.wordpress.runwithmusic.services.components.map.TrackingMap
import com.nourtech.wordpress.runwithmusic.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class TrackingFragment : Fragment() {
    private lateinit var binding: FragmentTrackingBinding
    private var isTracking = false
    private val viewModel: MainViewModel by viewModels()

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

        // persist map data
        binding.apply {
            mapView.onCreate(savedInstanceState)

            mapView.getMapAsync {
                viewModel.map = it
//                addAllPolylines()
            }

            // set on click listeners
            btnToggleRun.setOnClickListener {
                toggleRun()
            }
            btnFinishRun.setOnClickListener {
                stopRun()
            }

        }
        subscribeToObservers()

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
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }

    /* keep the camera focus on the path */
    private fun moveCameraToUser() {
        if (viewModel.path.value!!.isEmpty()) {
            viewModel.map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            viewModel.path.value!!.getLastLatLng(),
                            Constants.MAP_ZOOM
                    )
            )
        }
    }

    /* redraw all polylines after rotation */
    private fun addAllPolylines() {
        for (polyline in viewModel.path.value!!.getPolylines()) {
            val polyLineOptions = PolylineOptions()
                    .color(Constants.POLYLINE_COLOR)
                    .width(Constants.POLYLINE_WIDTH)
                    .addAll(polyline)
            viewModel.map?.addPolyline(polyLineOptions)
        }
    }

    private fun zoomToSeeWholeTrack() {
        val bounds = LatLngBounds.builder()
        for (polyline in viewModel.path.value!!.getPolylines())
            for (pos in polyline) {
                bounds.include(pos)
            }
        viewModel.map?.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                        bounds.build(),
                        binding.mapView.width,
                        binding.mapView.height,
                        (binding.mapView.height * 0.05F).toInt()
                )
        )
    }


    private fun sendCommandToService(action: String) =
            Intent(requireContext(), TrackingService::class.java).also {
                it.action = action
                requireContext().startService(it)
            }


    private fun subscribeToObservers() {
        stopwatch.timeRunInMillis.observe(viewLifecycleOwner) {
            binding.tvTimer.text =
                    TrackingUtility.getFormattedStopWatchTime(it, true)
        }
        TrackingService.isTracking.observe(viewLifecycleOwner) {
            isTracking = it
        }
        TrackingMap.latestLatLng.observe(viewLifecycleOwner) {
            viewModel.addPoint(it)
           // viewModel.addLatestPolyline()
            moveCameraToUser()
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