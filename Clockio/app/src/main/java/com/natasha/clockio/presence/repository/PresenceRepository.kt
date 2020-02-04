package com.natasha.clockio.presence.repository

import android.util.Log
import com.natasha.clockio.base.model.ApiResponse
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.base.util.ResponseUtils
import com.natasha.clockio.presence.service.PresenceApi
import com.natasha.clockio.presence.service.request.CheckinRequest
import com.natasha.clockio.presence.service.request.CheckoutRequest
import javax.inject.Inject

class PresenceRepository @Inject constructor(private val presenceApi: PresenceApi) {
  private val TAG = PresenceRepository::class.java.simpleName

  suspend fun sendCheckIn(id: String, request: CheckinRequest): BaseResponse<Any> {
    val response = presenceApi.checkIn(id, request)
    Log.d(TAG, "checkin $response")
    return ResponseUtils.convertResponse(response)
  }

  suspend fun sendCheckOut(id: String, request: CheckoutRequest): BaseResponse<Any> {
    val response = presenceApi.checkOut(id, request)
    Log.d(TAG, "checkout $response")
    return ResponseUtils.convertResponse(response)
  }
}