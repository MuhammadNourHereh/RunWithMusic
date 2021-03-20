package com.nourtech.wordpress.runwithmusic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nourtech.wordpress.runwithmusic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}