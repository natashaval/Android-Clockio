package com.natasha.clockio.home.repository

import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.home.service.ActivityApi
import java.util.*
import javax.inject.Inject

class ActivityRepository @Inject constructor(private val activityApi: ActivityApi) {
    suspend fun getActivityToday(id: String, date: String) : BaseResponse<Any> {
        val response = activityApi.getActivityToday(id, date)
        return try {
            if (response.isSuccessful) {
                BaseResponse.success(response.body())
            } else {
                BaseResponse.failed(response.errorBody())
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            BaseResponse.error(t.message, null)
        }
    }
}