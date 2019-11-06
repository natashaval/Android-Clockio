package com.natasha.clockio

import com.natasha.clockio.base.model.Test
import com.natasha.clockio.base.service.TestApi
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val testApi: TestApi) : MainRepository {

    override suspend fun getTest() = testApi.getTest()
}