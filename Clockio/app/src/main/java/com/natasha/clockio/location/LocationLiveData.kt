package com.natasha.clockio.location

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.ListenableWorker
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.natasha.clockio.base.constant.PreferenceConst
import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.location.service.LocationApi
import com.natasha.clockio.location.worker.LocationWorker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

//https://proandroiddev.com/android-tutorial-on-location-update-with-livedata-774f8fcc9f15
class LocationLiveData @Inject constructor(
  private val context: Context,
  private val locationApi: LocationApi,
  private val sharedPreferences: SharedPreferences
) : LiveData<LocationModel>() {
  private val TAG: String = LocationLiveData::class.java.simpleName
  private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

  override fun onInactive() {
    super.onInactive()
    fusedLocationClient.removeLocationUpdates(locationCallback)
  }

  override fun onActive() {
    super.onActive()
    val employeeId = sharedPreferences.getString(PreferenceConst.EMPLOYEE_ID_KEY, "")
    getLocation(employeeId!!)
  }

  fun getLocation(employeeId: String) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
      Log.d(TAG, "worker onActive locationLiveData $location")
      location?.let {
        setLocationData(it)
        sendLocationData(it, employeeId)
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

  private fun sendLocationData(location: Location, employeeId: String) {
    val loc = com.natasha.clockio.location.entity.Location(employeeId, location.latitude, location.longitude)
    val response = locationApi.sendLocation(loc).enqueue(object: Callback<DataResponse> {
      override fun onFailure(call: Call<DataResponse>, t: Throwable) {
        Log.e(TAG, "Worker send Error ${t.message}")
        t.printStackTrace()
      }

      override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
        if (response.isSuccessful) {
          Log.d(TAG, "Worker send Success ${response.body()}")
        } else {
          Log.d(TAG, "Worker send Failed ${response.errorBody()}")
        }
      }
    })
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
    const val MILLISECONDS: Long = 1000
    val locationRequest: LocationRequest = LocationRequest.create().apply {
      interval = (3 * 60 * MILLISECONDS)
      fastestInterval = (1 * 60 * MILLISECONDS)
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
  }
}