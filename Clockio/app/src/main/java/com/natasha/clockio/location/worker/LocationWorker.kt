package com.natasha.clockio.location.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import com.natasha.clockio.base.di.application.MyApplication
import com.natasha.clockio.location.entity.Location
import com.natasha.clockio.location.service.LocationApi
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val locationApi: LocationApi):
    Worker(context, params){

  //    https://proandroiddev.com/experiments-with-android-workmanager-our-new-reliable-assistant-for-deferrable-background-work-9baeb6bd7db3
  override fun doWork(): Result {
    Log.d(TAG, "Injected from Worker")
    /*val empId = params.inputData.getString(LOCATION_WORKER_EMP)
    val latitude = params.inputData.getDouble(LOCATION_WORKER_LATITUDE)
    val longitude = params.inputData.getDouble(LOCATION_WORKER_LONGITUDE)
    val location = Location(empId, latitude, longitude)
    val response = locationApi.sendLocation(location)*/
    return Result.success()
  }

  companion object {
//    https://medium.com/@aruke/workmanager-for-everyone-e6836e3ecfb9
    private const val TAG = "LocationWorker"
    private const val LOCATION_WORKER_EMP = "location_worker_'emp"
    private const val LOCATION_WORKER_LATITUDE = "location_worker_latitude"
    private const val LOCATION_WORKER_LONGITUDE = "location_worker_longitude"

    fun buildRequest(location: Location): OneTimeWorkRequest {
      val inputData = Data.Builder()
          .putString(LOCATION_WORKER_EMP, location.employeeId)
          .putDouble(LOCATION_WORKER_LATITUDE, location.latitude)
          .putDouble(LOCATION_WORKER_LONGITUDE, location.longitude)
          .build()

      val constraints = Constraints.Builder()
          .setRequiredNetworkType(NetworkType.CONNECTED)
          .build()

      return OneTimeWorkRequest.Builder(LocationWorker::class.java)
          .setInputData(inputData)
          .setConstraints(constraints)
          .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 2, TimeUnit.MINUTES)
          .build()
    }
  }
  //    https://android.jlelse.eu/injecting-into-workers-android-workmanager-and-dagger-948193c17684
  //    https://proandroiddev.com/dagger-2-setup-with-workmanager-a-complete-step-by-step-guild-bb9f474bde37
  /*class Factory @Inject constructor(
      val locationApi: LocationApi
  ): ChildWorkerFactory {
    override fun create(appContext: Context, params: WorkerParameters): Worker {
      return LocationWorker(appContext, params, locationApi)
    }
  }*/

//    https://medium.com/@neonankiti/how-to-use-dagger2-withworkmanager-bae3a5fb7dd3
    @AssistedInject.Factory
    interface Factory: ChildWorkerFactory

}