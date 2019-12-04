package com.natasha.clockio.base.di.module.repository

import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.home.service.EmployeeApi
import com.natasha.clockio.presence.repository.ImageRepository
import com.natasha.clockio.presence.repository.ImageRepositoryImpl
import com.natasha.clockio.presence.repository.PresenceRepository
import com.natasha.clockio.presence.service.EmotionApi
import com.natasha.clockio.presence.service.PresenceApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ImageModule {
  @Provides
  @ActivityScope
  fun provideImageRepository(): ImageRepository {
    return ImageRepositoryImpl()
  }

  @Provides
  @ActivityScope
  fun providePresenceApi(retrofit: Retrofit): PresenceApi {
    return retrofit.create(PresenceApi::class.java)
  }

  @Provides
  @ActivityScope
  fun provideEmotionApi(retrofit: Retrofit): EmotionApi {
    return retrofit.create(EmotionApi::class.java)
  }

  @Provides
  @ActivityScope
  fun providePresenceRepository(presenceApi: PresenceApi): PresenceRepository {
    return PresenceRepository(presenceApi)
  }
}