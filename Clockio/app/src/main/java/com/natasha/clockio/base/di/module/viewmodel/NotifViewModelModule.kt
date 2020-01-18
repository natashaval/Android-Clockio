package com.natasha.clockio.base.di.module.viewmodel

import androidx.lifecycle.ViewModel
import com.natasha.clockio.base.di.qualifier.ViewModelKey
import com.natasha.clockio.notification.ui.NotifViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NotifViewModelModule {
  @Binds
  @IntoMap
  @ViewModelKey(NotifViewModel::class)
  abstract fun bindNotifViewModel(viewModel: NotifViewModel): ViewModel
}