package com.natasha.clockio.base.di.module

import com.natasha.clockio.login.service.AuthApi
import com.natasha.clockio.base.service.TestApi
import com.natasha.clockio.base.util.LiveDataCallAdapterFactory
import com.natasha.clockio.home.service.ActivityApi
import com.natasha.clockio.home.service.EmployeeApi
import com.natasha.clockio.location.service.LocationApi
import com.natasha.clockio.notification.service.NotifApi
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

    @Provides
    fun provideEmployeeApi(retrofit: Retrofit): EmployeeApi {
        return retrofit.create(EmployeeApi::class.java)
    }

    @Provides
    fun provideActivityApi(retrofit: Retrofit): ActivityApi {
        return retrofit.create(ActivityApi::class.java)
    }

    @Provides
    fun provideLocationApi(retrofit: Retrofit): LocationApi {
        return retrofit.create(LocationApi::class.java)
    }
}