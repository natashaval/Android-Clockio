package com.natasha.clockio.base.di.module.viewmodel

import androidx.lifecycle.ViewModel
import com.natasha.clockio.base.di.qualifier.ViewModelKey
import com.natasha.clockio.presence.viewModel.ImageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ImageViewModelModule {
  @Binds
  @IntoMap
  @ViewModelKey(ImageViewModel::class)
  abstract fun bindImageViewModel(viewModel: ImageViewModel): ViewModel
}