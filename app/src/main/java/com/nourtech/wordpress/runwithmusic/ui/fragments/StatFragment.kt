package com.nourtech.wordpress.runwithmusic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nourtech.wordpress.runwithmusic.databinding.FragmentSettingsBinding
import com.nourtech.wordpress.runwithmusic.databinding.FragmentStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatFragment : Fragment() {
    private lateinit var binding: FragmentStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }
}
