package com.nourtech.wordpress.runwithmusic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nourtech.wordpress.runwithmusic.databinding.FragmentTrackingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TrackingFragment : Fragment() {
    private lateinit var binding: FragmentTrackingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }
}