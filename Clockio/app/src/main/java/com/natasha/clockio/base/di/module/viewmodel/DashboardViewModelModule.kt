package com.natasha.clockio.base.di.module.viewmodel

import androidx.lifecycle.ViewModel
import com.natasha.clockio.base.di.qualifier.ViewModelKey
import com.natasha.clockio.home.viewmodel.DashboardViewModel
import com.natasha.clockio.home.viewmodel.FriendViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DashboardViewModelModule {
  @Binds
  @IntoMap
  @ViewModelKey(DashboardViewModel::class)
  abstract fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel
}