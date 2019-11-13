package com.natasha.clockio.base.di.module

import com.natasha.clockio.base.di.qualifier.ActivityContext
import com.natasha.clockio.login.service.AuthApi
import com.natasha.clockio.base.service.TestApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [RetrofitModule::class])
class ApiModule {

    @Provides
    fun provideTestApi(retrofit: Retrofit): TestApi {
        return retrofit.create(TestApi::class.java)
    }

    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}