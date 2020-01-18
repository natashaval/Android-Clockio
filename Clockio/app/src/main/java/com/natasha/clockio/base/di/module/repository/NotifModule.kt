package com.natasha.clockio.base.di.module.repository

import com.natasha.clockio.base.database.AppDatabase
import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.notification.dao.NotifDao
import com.natasha.clockio.notification.dao.NotifLocalCache
import com.natasha.clockio.notification.repository.NotifRepository
import com.natasha.clockio.notification.service.NotifApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import java.util.concurrent.Executor

@Module
class NotifModule {
    @Provides
    @ActivityScope
    fun provideNotifDao(database: AppDatabase) = database.notifDao()


    @Provides
    @ActivityScope
    fun provideNotifApi(retrofit: Retrofit): NotifApi {
        return retrofit.create(NotifApi::class.java)
    }

    @Provides
    @ActivityScope
    fun provideNotifLocalCache(notifDao: NotifDao, ioExecutor: Executor): NotifLocalCache {
        return NotifLocalCache(notifDao, ioExecutor)
    }
}