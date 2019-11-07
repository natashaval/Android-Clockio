package com.natasha.clockio.base.di.module

import com.natasha.clockio.MainActivity
import com.natasha.clockio.base.di.module.activity.LoginActivityModule
import com.natasha.clockio.base.di.module.activity.MainActivityModule
import com.natasha.clockio.base.di.module.repository.LoginModule
import com.natasha.clockio.base.di.module.repository.TestModule
import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.base.di.viewmodel.LoginViewModelModule
import com.natasha.clockio.base.di.viewmodel.MainViewModelModule
import com.natasha.clockio.login.ui.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuilderModule {
    @ContributesAndroidInjector(modules = [MainActivityModule::class, TestModule::class, MainViewModelModule::class])
    @ActivityScope
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [LoginActivityModule::class, LoginModule::class, LoginViewModelModule::class])
    @ActivityScope
    internal abstract fun bindLoginActivity(): LoginActivity
}