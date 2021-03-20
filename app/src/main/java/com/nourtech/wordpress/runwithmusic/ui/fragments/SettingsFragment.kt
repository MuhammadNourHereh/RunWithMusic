package com.nourtech.wordpress.runtracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.nourtech.wordpress.runtracker.R
import com.nourtech.wordpress.runtracker.databinding.FragmentSettingsBinding
import com.nourtech.wordpress.runtracker.others.Constants.KEY_NAME
import com.nourtech.wordpress.runtracker.others.Constants.KEY_WEIGHT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPref: SharedPreferences

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(inflater)

        binding.btnApplyChanges.setOnClickListener {

            // make the massage
            val massage: String = if (applyChangesToSharedPreference())
                "preferences update successfully"
            else
                "fields are empty or weight is zero"

            Snackbar.make(requireView(), massage, Snackbar.LENGTH_SHORT)
                .show()
        }

        return binding.root
    }


    private fun applyChangesToSharedPreference(): Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()

        if (name.isEmpty() || weight.isEmpty())
            return false
        if (weight.toFloat() == 0f)
            return false

        sharedPref.edit()
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putString(KEY_NAME, name)
            .apply()
        return true
    }
}