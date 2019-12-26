package com.natasha.clockio.base.di.module

import android.app.Application
import android.content.Context
import com.natasha.clockio.base.di.qualifier.ApplicationContext
import com.natasha.clockio.base.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Module(includes = [
    SharedPrefModule::class, DatabaseModule::class])
class ContextModule {

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @ApplicationScope
    fun provideExecutors(): Executor {
        return Executors.newCachedThreadPool()
    }
}
