package com.natasha.clockio.location

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import javax.inject.Inject

//class LocationViewModel: ViewModel() {
class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val locationData = LocationLiveData(application)

    fun getLocationData() = locationData
}