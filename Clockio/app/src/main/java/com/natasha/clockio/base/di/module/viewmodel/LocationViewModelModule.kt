package com.natasha.clockio.base.di.module.viewmodel

import androidx.lifecycle.ViewModel
import com.natasha.clockio.base.di.module.worker.LocationWorkerModule
import com.natasha.clockio.base.di.qualifier.ViewModelKey
import com.natasha.clockio.location.LocationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [LocationWorkerModule::class])
abstract class LocationViewModelModule {
  @Binds
  @IntoMap
  @ViewModelKey(LocationViewModel::class)
  abstract fun bindLocationViewModel(viewModel: LocationViewModel): ViewModel
}