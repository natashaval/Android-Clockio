package com.natasha.clockio.base.di.module.worker

import androidx.work.Worker
import com.natasha.clockio.base.di.qualifier.WorkerKey
import com.natasha.clockio.location.worker.ChildWorkerFactory
import com.natasha.clockio.location.worker.LocationWorker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

//https://proandroiddev.com/dagger-2-setup-with-workmanager-a-complete-step-by-step-guild-bb9f474bde37
@Module
interface WorkerBindingModule {
  @Binds
  @IntoMap
  @WorkerKey(LocationWorker::class)
  fun bindLocationWorker(worker: LocationWorker.Factory): ChildWorkerFactory

}