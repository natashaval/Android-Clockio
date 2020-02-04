package com.natasha.clockio.base.di.module.repository

import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.presence.repository.PresenceRepository
import com.natasha.clockio.presence.service.PresenceApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class PresenceModule {
    @Provides
    @ActivityScope
    fun providePresenceApi(retrofit: Retrofit): PresenceApi {
        return retrofit.create(PresenceApi::class.java)
    }

    @Provides
    @ActivityScope
    fun providePresenceRepository(presenceApi: PresenceApi): PresenceRepository {
        return PresenceRepository(presenceApi)
    }
}