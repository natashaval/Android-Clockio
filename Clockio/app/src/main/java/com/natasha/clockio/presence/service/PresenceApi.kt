package com.natasha.clockio.presence.service

import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.presence.service.request.CheckinRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PresenceApi {
  @POST("/api/presence/{id}/checkin")
  suspend fun checkIn(
      @Path("id") id: String,
      @Body request: CheckinRequest)
      : Response<DataResponse>
}