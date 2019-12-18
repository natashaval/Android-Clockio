package com.natasha.clockio.home.service

import com.natasha.clockio.home.entity.Activity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface ActivityApi {
    @GET("/api/activity/employee/{id}/today")
    suspend fun getActivityToday(@Path("id") id: String, @Query("date") date: String)
    : Response<List<Activity>>
}