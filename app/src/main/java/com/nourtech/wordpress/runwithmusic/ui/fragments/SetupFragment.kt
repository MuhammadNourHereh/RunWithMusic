package com.nourtech.wordpress.runtracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nourtech.wordpress.runtracker.R
import com.nourtech.wordpress.runtracker.databinding.FragmentSetupBinding
import com.nourtech.wordpress.runtracker.others.Constants.KEY_FIRST_TIME_TOGGLE
import com.nourtech.wordpress.runtracker.others.Constants.KEY_NAME
import com.nourtech.wordpress.runtracker.others.Constants.KEY_WEIGHT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    private lateinit var binding: FragmentSetupBinding

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetupBinding.inflate(inflater, container, false)
        binding.tvContinue.setOnClickListener {
            if (writePersonalDataToSharedPreferences()) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                Snackbar.make(requireView(), "please enter all the fields", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        return binding.root
    }

    private fun writePersonalDataToSharedPreferences(): Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()

        // check if a field is empty
        if (name.isEmpty() || weight.isEmpty())
            return false

        sharedPref.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()

        // tool bar set name
        return true

    }
}