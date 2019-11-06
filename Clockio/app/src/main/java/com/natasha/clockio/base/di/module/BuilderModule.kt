package com.natasha.clockio.base.di.module

import com.natasha.clockio.MainActivity
import com.natasha.clockio.base.di.module.activity.LoginActivityModule
import com.natasha.clockio.base.di.module.activity.MainActivityModule
import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.login.ui.login.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuilderModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [LoginActivityModule::class])
    internal abstract fun bindLoginActivity(): LoginActivity
}
