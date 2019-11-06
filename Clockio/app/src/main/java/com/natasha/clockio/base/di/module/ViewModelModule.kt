package com.natasha.clockio.base.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.natasha.clockio.MainRepository
import com.natasha.clockio.MainViewModel
import com.natasha.clockio.base.di.qualifier.ViewModelKey
import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.base.di.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

//https://medium.com/@marco_cattaneo/android-viewmodel-and-factoryprovider-good-way-to-manage-it-with-dagger-2-d9e20a07084c
//https://www.youtube.com/watch?v=DToD1W9WdsE
// dimasukkan satu-satu view modelnya lewat intoMap, sedangkan yang viewmodelfactory ikut yang application scope
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}