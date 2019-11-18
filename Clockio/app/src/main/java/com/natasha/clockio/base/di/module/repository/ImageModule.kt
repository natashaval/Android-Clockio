package com.natasha.clockio.base.di.module.repository

import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.presence.repository.ImageRepository
import com.natasha.clockio.presence.repository.ImageRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class ImageModule {
  @Provides
  @ActivityScope
  fun provideImageRepository(): ImageRepository {
    return ImageRepositoryImpl()
  }
}