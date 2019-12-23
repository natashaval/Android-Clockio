package com.natasha.clockio.location

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import javax.inject.Inject

//class LocationViewModel: ViewModel() {
class LocationViewModel @Inject constructor(context: Context) : ViewModel() {
    private val locationData = LocationLiveData(context)

    fun getLocationData() = locationData
}