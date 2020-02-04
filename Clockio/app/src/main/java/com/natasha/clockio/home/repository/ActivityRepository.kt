package com.natasha.clockio.home.repository

import android.util.Log
import com.natasha.clockio.activity.entity.ActivityCreateRequest
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.base.model.PageResponse
import com.natasha.clockio.base.util.ResponseUtils
import com.natasha.clockio.home.entity.Activity
import com.natasha.clockio.home.service.ActivityApi
import java.util.*
import javax.inject.Inject

class ActivityRepository @Inject constructor(private val activityApi: ActivityApi) {
  private val TAG: String = ActivityRepository::class.java.simpleName

  suspend fun getActivityToday(id: String, date: String) : BaseResponse<Any> {
    val response = activityApi.getActivityToday(id, date)
    return ResponseUtils.convertResponse(response)
  }

  suspend fun createActivity(empId: String, request: ActivityCreateRequest): BaseResponse<Any> {
    val response = activityApi.createActivity(empId, request)
    return ResponseUtils.convertResponse(response)
  }

  suspend fun deleteActivity(id: String): BaseResponse<Any> {
    val response = activityApi.deleteActivity(id)
    return ResponseUtils.convertResponse(response)
  }

  suspend fun getActivityHistory(id:String, start: String, end: String, page: Int?, size: Int?,
      onSuccess: (empList: PageResponse<Activity>) -> Unit,
      onError: (error: String) -> Unit) {
    val response = activityApi.getActivityHistory(id, start, end, page, size)
    try {
      if (response.isSuccessful) {
        Log.d(TAG, "history response body")
        response.body()?.let {
          onSuccess(it)
        }
      } else {
        onError(response.errorBody()?.toString() ?: "unkown error")
      }
    } catch (t: Throwable) {
      Log.d(TAG, "history getAll failed ${t.message}")
      onError(t.message?: "unknown error")
    }
  }
}