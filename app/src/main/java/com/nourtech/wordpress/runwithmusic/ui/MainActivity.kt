package com.nourtech.wordpress.runwithmusic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.nourtech.wordpress.runwithmusic.R
import com.nourtech.wordpress.runwithmusic.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // setup nav host
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment

        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navController.graph = navGraph

        // setup bottom nav view
        binding.bottomNavigationView.setupWithNavController(navController)

    }
}