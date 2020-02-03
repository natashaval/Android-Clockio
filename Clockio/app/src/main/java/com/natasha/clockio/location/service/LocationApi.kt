package com.natasha.clockio.location.service

import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.location.entity.Location
import com.natasha.clockio.location.entity.LocationModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface LocationApi {
    @POST("/api/location")
    fun sendLocation(@Body location: Location): Call<DataResponse>

    @GET("/api/location/{id}/history")
    suspend fun getLocationHistory(@Path("id") id: String,
                                   @Query("start") start: String, @Query("end") end: String)
    : Response<ArrayList<LocationModel>>
}