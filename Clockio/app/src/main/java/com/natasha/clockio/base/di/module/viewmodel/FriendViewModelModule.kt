package com.natasha.clockio.base.di.module.viewmodel

import androidx.lifecycle.ViewModel
import com.natasha.clockio.base.di.qualifier.ViewModelKey
import com.natasha.clockio.home.viewmodel.FriendViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FriendViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FriendViewModel::class)
    abstract fun bindFriendViewModel(viewModel: FriendViewModel): ViewModel
}