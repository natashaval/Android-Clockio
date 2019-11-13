package com.natasha.clockio.base.di.module.repository

import com.natasha.clockio.MainRepository
import com.natasha.clockio.MainRepositoryImpl
import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.base.service.TestApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class TestModule {

    @Provides
    @ActivityScope
    fun provideMainRepository(testApi: TestApi): MainRepository {
        return MainRepositoryImpl(testApi)
    }
}