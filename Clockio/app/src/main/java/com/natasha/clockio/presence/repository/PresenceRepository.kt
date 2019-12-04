package com.natasha.clockio.presence.repository

import android.util.Log
import com.natasha.clockio.base.model.ApiResponse
import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.presence.service.PresenceApi
import com.natasha.clockio.presence.service.request.CheckinRequest
import javax.inject.Inject

class PresenceRepository @Inject constructor(private val presenceApi: PresenceApi) {
  private val TAG = PresenceRepository::class.java.simpleName

  suspend fun sendCheckIn(id: String, request: CheckinRequest): ApiResponse<DataResponse> {
    val response = presenceApi.checkIn(id, request)
    Log.d(TAG, "checkin $response")
    return ApiResponse.create(response)
  }
}