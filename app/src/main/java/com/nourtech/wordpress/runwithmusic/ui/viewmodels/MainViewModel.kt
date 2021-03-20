package com.nourtech.wordpress.runwithmusic.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.nourtech.wordpress.runwithmusic.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repo: MainRepository): ViewModel()  {

}