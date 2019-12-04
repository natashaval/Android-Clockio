package com.natasha.clockio.base.di.module.viewmodel

import androidx.lifecycle.ViewModel
import com.natasha.clockio.base.di.qualifier.ViewModelKey
import com.natasha.clockio.presence.viewModel.ImageViewModel
import com.natasha.clockio.presence.viewModel.PresenceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

// per fitur Presence

@Module
abstract class PresenceViewModelModule {
  @Binds
  @IntoMap
  @ViewModelKey(ImageViewModel::class)
  abstract fun bindImageViewModel(viewModel: ImageViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(PresenceViewModel::class)
  abstract fun bindPresenceViewModel(viewModel: PresenceViewModel): ViewModel
}