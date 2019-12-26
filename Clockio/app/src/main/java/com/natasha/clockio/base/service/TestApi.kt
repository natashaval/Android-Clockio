package com.natasha.clockio.base.service

import com.natasha.clockio.base.model.Test
import com.natasha.clockio.base.model.LoggedInUser
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface TestApi {
//    @GET("/bins/hh9vk")
    @GET("/test")
    suspend fun getTest() : Response<Test>

//    @GET("/bins/n3g1g")
    @GET("/test")
    suspend fun getTestAutomatically() : Response<Test>
}