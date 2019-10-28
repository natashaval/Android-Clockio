package com.natasha.clockio.base.service

import com.natasha.clockio.base.model.Test
import retrofit2.Call
import retrofit2.http.GET

interface TestService {
    @GET("/bins/hh9vk")
    fun getTest()
    : Call<Test>
}