package com.natasha.clockio.location

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

//https://proandroiddev.com/android-tutorial-on-location-update-with-livedata-774f8fcc9f15
class LocationLiveData(context: Context) : LiveData<LocationModel>() {
  private val TAG: String = LocationLiveData::class.java.simpleName
  private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

  override fun onInactive() {
    super.onInactive()
    fusedLocationClient.removeLocationUpdates(locationCallback)
  }

  override fun onActive() {
    super.onActive()
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
      Log.d(TAG, "onActive locationLiveData $location")
      location?.let {
        setLocationData(it)
      }
    }
    startLocationUpdates()
  }

  private fun setLocationData(location: Location) {
    value = LocationModel(
        latitude = location.latitude,
        longitude = location.longitude
    )
  }

  private val locationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult?) {
      locationResult ?: return
      Log.d(TAG, "locationCallback $locationResult")
      for (location in locationResult.locations) {
        setLocationData(location)
      }
    }
  }

  private fun startLocationUpdates() {
    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        null
    )
  }

  companion object {
    val locationRequest: LocationRequest = LocationRequest.create().apply {
      interval = 10000
      fastestInterval = 5000
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
  }
}