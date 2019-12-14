package com.natasha.clockio.base.di.module.viewmodel

import androidx.lifecycle.ViewModel
import com.natasha.clockio.base.di.qualifier.ViewModelKey
import com.natasha.clockio.home.viewmodel.ActivityViewModel
import com.natasha.clockio.home.viewmodel.ProfileViewModel
import com.natasha.clockio.location.LocationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileViewModelModule {
  @Binds
  @IntoMap
  @ViewModelKey(ProfileViewModel::class)
  abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(ActivityViewModel::class)
  abstract fun bindActivityViewModel(viewModel: ActivityViewModel): ViewModel
}