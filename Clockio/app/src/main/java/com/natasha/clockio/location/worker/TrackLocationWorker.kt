package com.natasha.clockio.location.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.natasha.clockio.location.LocationLiveData
import java.lang.Exception
import javax.inject.Inject

class TrackLocationWorker @Inject constructor(
    context: Context,
    private val params: WorkerParameters
): Worker(context, params) {

    companion object {
    private val TAG: String = TrackLocationWorker::class.java.simpleName
        const val LOCATION_WORKER_EMPLOYEE_ID = "LOCATION_WORKER_EMPLOYEE_ID"
    }
    @Inject lateinit var locationLiveData: LocationLiveData

    override fun doWork(): Result {
        val empId = params.inputData.getString(LOCATION_WORKER_EMPLOYEE_ID)
        return try {
            locationLiveData.getLocation(empId!!)
            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, "failed in worker ${e.message}")
            Result.failure()
        }
    }

}