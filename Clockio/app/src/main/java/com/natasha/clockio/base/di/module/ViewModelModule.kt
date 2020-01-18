package com.natasha.clockio.base.di.module

import androidx.lifecycle.ViewModel
import com.natasha.clockio.MainViewModel
import com.natasha.clockio.base.di.qualifier.ViewModelKey
import com.natasha.clockio.login.ui.LoginViewModel
import dagger.Binds
import dagger.Module
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

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
}