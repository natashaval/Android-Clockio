package com.natasha.clockio.base.di.module.worker

import androidx.work.Worker
import androidx.work.WorkerFactory
import com.natasha.clockio.location.worker.DaggerWorkerFactory
import dagger.Binds
import dagger.Module

@Module
abstract class WorkerFactoryModule {
  @Binds
  abstract fun bindWorkerFactory(worker: DaggerWorkerFactory): WorkerFactory
}