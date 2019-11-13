package com.natasha.clockio.base.di.module

import androidx.lifecycle.ViewModelProvider
import com.natasha.clockio.base.di.scope.ApplicationScope
import com.natasha.clockio.base.di.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}