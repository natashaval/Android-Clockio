package com.natasha.clockio.base.di.module

import android.app.Application
import android.content.Context
import com.natasha.clockio.base.di.qualifier.ApplicationContext
import com.natasha.clockio.base.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module(includes = [
    SharedPrefModule::class])
class ContextModule {

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideContext(application: Application): Context {
        return application
    }
}