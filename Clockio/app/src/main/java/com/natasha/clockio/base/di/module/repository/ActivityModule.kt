package com.natasha.clockio.base.di.module.repository

import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.home.repository.ActivityRepository
import com.natasha.clockio.home.service.ActivityApi
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    @Provides
    @ActivityScope
    fun provideActivityRepository(activityApi: ActivityApi): ActivityRepository {
        return ActivityRepository(activityApi)
    }
}