package com.natasha.clockio

import com.natasha.clockio.base.model.Test
import retrofit2.Response

interface MainRepository {

    suspend fun getTest() : Response<Test>
    suspend fun getTestAuto(): Response<Test>
}