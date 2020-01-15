package com.natasha.clockio.location.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import com.natasha.clockio.location.entity.Location
import com.natasha.clockio.location.service.LocationApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class LocationWorker(
    private val context: Context,
    private val params: WorkerParameters,
    private val locationApi: LocationApi):
    Worker(context, params){

  //    https://proandroiddev.com/experiments-with-android-workmanager-our-new-reliable-assistant-for-deferrable-background-work-9baeb6bd7db3
  override fun doWork(): Result {
    val empId = params.inputData.getString(LOCATION_WORKER_EMP)
    val latitude = params.inputData.getDouble(LOCATION_WORKER_LATITUDE, 0.0)
    val longitude = params.inputData.getDouble(LOCATION_WORKER_LONGITUDE, 0.0)
    val location = Location(empId!!, latitude, longitude)
//    Log.d(TAG, "Injected from Worker $location")
    val response = locationApi.sendLocation(location).execute()
    if (response.isSuccessful) {
      Log.d(TAG, "Worker Success")
        return Result.success()
    } else {
        if (response.code() in (500..599)) {
            return Result.retry()
        }
        return Result.failure()
    }
  }

  companion object {
//    https://medium.com/@aruke/workmanager-for-everyone-e6836e3ecfb9
    private const val TAG = "LocationWorker"
    const val LOCATION_WORKER_TAG = "location-worker"
    const val LOCATION_WORKER_EMP = "location_worker_emp"
    const val LOCATION_WORKER_LATITUDE = "location_worker_latitude"
    const val LOCATION_WORKER_LONGITUDE = "location_worker_longitude"

    fun buildOneTimeRequest(location: Location): OneTimeWorkRequest {
      val inputData = Data.Builder()
          .putString(LOCATION_WORKER_EMP, location.employeeId)
          .putDouble(LOCATION_WORKER_LATITUDE, location.latitude)
          .putDouble(LOCATION_WORKER_LONGITUDE, location.longitude)
          .build()

      val constraints = Constraints.Builder()
          .setRequiredNetworkType(NetworkType.CONNECTED)
          .build()

      return OneTimeWorkRequest.Builder(LocationWorker::class.java)
          .addTag(LOCATION_WORKER_TAG)
          .setInputData(inputData)
          .setConstraints(constraints)
          .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
          .build()
    }

      fun buildPeriodicRequest(location: Location): PeriodicWorkRequest {
          val inputData = Data.Builder()
              .putString(LOCATION_WORKER_EMP, location.employeeId)
              .putDouble(LOCATION_WORKER_LATITUDE, location.latitude)
              .putDouble(LOCATION_WORKER_LONGITUDE, location.longitude)
              .build()

          val constraints = Constraints.Builder()
              .setRequiredNetworkType(NetworkType.CONNECTED)
              .build()

          return PeriodicWorkRequest.Builder(LocationWorker::class.java, 2, TimeUnit.MINUTES)
              .setInputData(inputData)
              .setConstraints(constraints)
              .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.MINUTES)
              .build()
      }
  }
  //    https://android.jlelse.eu/injecting-into-workers-android-workmanager-and-dagger-948193c17684
  //    https://proandroiddev.com/dagger-2-setup-with-workmanager-a-complete-step-by-step-guild-bb9f474bde37
  class Factory @Inject constructor(
      val locationApi: Provider<LocationApi>
  ): ChildWorkerFactory {
    override fun create(appContext: Context, params: WorkerParameters): Worker {
      return LocationWorker(appContext, params, locationApi.get())
    }
  }
}