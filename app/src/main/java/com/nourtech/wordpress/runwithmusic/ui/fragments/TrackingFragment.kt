package com.nourtech.wordpress.runtracker.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nourtech.wordpress.runtracker.R
import com.nourtech.wordpress.runtracker.databinding.FragmentTrackingBinding
import com.nourtech.wordpress.runtracker.db.RunEntity
import com.nourtech.wordpress.runtracker.others.Constants.ACTION_PAUSE_SERVICE
import com.nourtech.wordpress.runtracker.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.nourtech.wordpress.runtracker.others.Constants.ACTION_STOP_SERVICE
import com.nourtech.wordpress.runtracker.others.Constants.MAP_ZOOM
import com.nourtech.wordpress.runtracker.others.Constants.POLYLINE_COLOR
import com.nourtech.wordpress.runtracker.others.Constants.POLYLINE_WIDTH
import com.nourtech.wordpress.runtracker.others.TrackingUtility
import com.nourtech.wordpress.runtracker.others.TrackingUtility.getFormattedStopWatchTime
import com.nourtech.wordpress.runtracker.services.Polyline
import com.nourtech.wordpress.runtracker.services.TrackingService
import com.nourtech.wordpress.runtracker.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    @set:Inject
    var weight: Float = 80f
    private val viewModel: MainViewModel by viewModels()
    private var map: GoogleMap? = null

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private lateinit var binding: FragmentTrackingBinding

    private var currentTimeMilli = 0L

    private var menu: Menu? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackingBinding.inflate(inflater, container, false)

        binding.mapView.onCreate(savedInstanceState)

        // setup on click listeners
        binding.btnToggleRun.setOnClickListener {
            toggleRun()
        }
        binding.btnFinishRun.setOnClickListener {
            endRunAndSaveToDb()
        }

        // update when recreate the fragment
        binding.mapView.getMapAsync {
            map = it
            addAllPolyline()
        }

        // setup menu
        setHasOptionsMenu(true)

        subscribeToService()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_tracking_menu, menu)

        menu.getItem(0).isVisible = true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miCancelTracking -> {
                showCancelTrackingDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCancelTrackingDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setIcon(R.drawable.ic_delete)
            .setTitle("Cancel the run ?")
            .setMessage("Are you sure to cancel the run ?")
            .setPositiveButton("Yes") { _, _ ->
                stopRun()
            }.setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }.create()
        dialog.show()
    }

    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }

    /* keep update with the service */
    private fun subscribeToService() {
        TrackingService.isTracking.observe(viewLifecycleOwner) {
            updateTracking(it)
        }

        TrackingService.pathPoints.observe(viewLifecycleOwner) {
            pathPoints = it
            moveCameraToUser()
            addLatestPolyline()
        }

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner) {
            currentTimeMilli = it
            binding.tvTimer.text = getFormattedStopWatchTime(it, true)
        }
    }

    private fun toggleRun() {
        if (isTracking) {
            menu?.getItem(0)?.isVisible = true
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    /* update the ui*/
    private fun updateTracking(isTracking: Boolean) {

        this.isTracking = isTracking
        if (isTracking) {
            binding.btnToggleRun.text = getString(R.string.start)
            binding.btnFinishRun.visibility = View.VISIBLE
        } else {
            binding.btnToggleRun.text = getString(R.string.stop)
            menu?.getItem(0)?.isVisible = true
            binding.btnFinishRun.visibility = View.GONE
        }
    }

    /* keep the camera focus on the path */
    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    /* redraw all polylines after rotation */
    private fun addAllPolyline() {
        for (polyline in pathPoints) {
            val polyLineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polyLineOptions)
        }
    }

    /* add a new poly line */
    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polyLineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polyLineOptions)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    private fun zoomToSeeWholeTrack() {
        val bounds = LatLngBounds.builder()
        for (polyline in pathPoints)
            for (pos in polyline) {
                bounds.include(pos)
            }
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                binding.mapView.width,
                binding.mapView.height,
                (binding.mapView.height * 0.05F).toInt()
            )
        )
    }

    private fun endRunAndSaveToDb() {
        map?.snapshot { bmp ->

            zoomToSeeWholeTrack()

            var distanceInMeters = 0
            for (polyline in pathPoints) {
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
            }
            val avgSpeed =
                round((distanceInMeters / 1000f) / (currentTimeMilli / 1000f / 60 / 60) * 10) / 10f
            val dateTimestamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()
            val run = RunEntity(
                bmp,
                dateTimestamp,
                avgSpeed,
                distanceInMeters,
                currentTimeMilli,
                caloriesBurned
            )

            viewModel.insertRun(run)
            Snackbar.make(
                requireActivity().findViewById(R.id.rootView),
                "Run saved successfully",
                Snackbar.LENGTH_LONG
            ).show()
            stopRun()
        }
    }
}