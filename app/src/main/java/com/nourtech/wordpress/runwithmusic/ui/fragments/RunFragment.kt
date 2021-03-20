package com.nourtech.wordpress.runwithmusic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nourtech.wordpress.runwithmusic.R
import com.nourtech.wordpress.runwithmusic.databinding.FragmentRunBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunFragment() : Fragment() {
    private lateinit var binding: FragmentRunBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
    }
}

