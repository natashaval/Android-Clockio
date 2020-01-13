package com.natasha.clockio.location.service

import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.location.entity.Location
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationApi {
    @POST("/api/location")
    fun sendLocation(@Body location: Location): Call<Response<DataResponse>>
}