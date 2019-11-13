package com.natasha.clockio

import com.natasha.clockio.base.model.Test
import com.natasha.clockio.base.service.TestApi
import retrofit2.Response
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val testApi: TestApi) : MainRepository {

    override suspend fun getTest() = testApi.getTest()

    override suspend fun getTestAuto(): Response<Test> {
        return testApi.getTestAutomatically()
    }
}