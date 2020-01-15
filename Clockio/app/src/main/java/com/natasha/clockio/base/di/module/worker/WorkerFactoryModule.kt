package com.natasha.clockio.base.di.module.worker

import androidx.work.WorkerFactory
import com.natasha.clockio.base.di.factory.DaggerWorkerFactory
import dagger.Binds
import dagger.Module

@Module
abstract class WorkerFactoryModule {
    @Binds
    abstract fun bindDaggerWorkerFactory(factory: DaggerWorkerFactory): WorkerFactory
}