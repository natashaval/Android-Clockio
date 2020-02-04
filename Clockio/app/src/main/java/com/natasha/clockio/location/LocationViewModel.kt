package com.natasha.clockio.location

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import androidx.work.*
import com.natasha.clockio.base.constant.PreferenceConst
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.location.entity.LocationModel
import com.natasha.clockio.location.service.LocationApi
import com.natasha.clockio.location.worker.LocationWorker
import com.natasha.clockio.location.worker.TrackLocationWorker
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocationViewModel @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val locationApi: LocationApi
) : ViewModel() {

  companion object {
    const val LOCATION_WORKER_TAG = "LOCATION_WORKER_TAG"
    private val TAG: String = LocationViewModel::class.java.simpleName
  }

  private val locationData = LocationLiveData(context, locationApi, sharedPreferences)
  fun getLocationData() = locationData

  private val _locationHistory = MutableLiveData<ArrayList<LocationModel>>()
  val locationHistory: LiveData<ArrayList<LocationModel>>
    get() = _locationHistory

  fun trackLocation() {
    val empId = sharedPreferences.getString(PreferenceConst.EMPLOYEE_ID_KEY, "")
    val inputData = Data.Builder()
        .putString(LocationWorker.LOCATION_WORKER_EMP, empId)
        .build()

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val locationWorker = PeriodicWorkRequest.Builder(TrackLocationWorker::class.java, 20, TimeUnit.MINUTES)
        .addTag(LOCATION_WORKER_TAG)
        .setInputData(inputData)
        .setConstraints(constraints)
        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.MINUTES)
        .build()

    Log.d(TAG, "worker start location update")
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(LOCATION_WORKER_TAG, ExistingPeriodicWorkPolicy.KEEP, locationWorker)
  }

  fun stopTrackLocation() {
    WorkManager.getInstance(context).cancelAllWorkByTag(LOCATION_WORKER_TAG)
    Log.d(TAG, "worker stop location update")
  }

  fun getLocationHistory(empId: String, start: String, end: String) {
    viewModelScope.launch {
      val response = locationApi.getLocationHistory(empId, start, end)
      if (response.isSuccessful) {
        response.body()?.let {
          _locationHistory.value = it
        }
      }
    }
  }
}