package com.natasha.clockio.base.service

import com.natasha.clockio.base.model.Test
import com.natasha.clockio.base.model.LoggedInUser
import retrofit2.Call
import retrofit2.http.GET

interface TestService {
//    @GET("/bins/hh9vk")
    @GET("/test")
    fun getTest() : Call<Test>

    @GET("/api/profile")
    fun getProfile() : Call<LoggedInUser>
}