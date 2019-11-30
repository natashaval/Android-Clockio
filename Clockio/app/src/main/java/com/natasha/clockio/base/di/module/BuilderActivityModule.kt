package com.natasha.clockio.base.di.module

import com.natasha.clockio.MainActivity
import com.natasha.clockio.base.di.module.activity.LoginActivityModule
import com.natasha.clockio.base.di.module.activity.MainActivityModule
import com.natasha.clockio.base.di.module.repository.ProfileModule
import com.natasha.clockio.base.di.module.repository.LoginModule
import com.natasha.clockio.base.di.module.repository.TestModule
import com.natasha.clockio.base.di.module.viewmodel.HomeViewModelModule
import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.base.di.module.viewmodel.LoginViewModelModule
import com.natasha.clockio.base.di.module.viewmodel.MainViewModelModule
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.login.ui.LoginActivity
import com.natasha.clockio.presence.ui.PresenceActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [BuilderFragmentModule::class])
abstract class BuilderActivityModule {
    @ContributesAndroidInjector(modules = [MainActivityModule::class, TestModule::class, MainViewModelModule::class])
    @ActivityScope
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [LoginActivityModule::class, LoginModule::class, LoginViewModelModule::class])
    @ActivityScope
    internal abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    @ActivityScope
    internal abstract fun bindPresenceActivity(): PresenceActivity

    @ContributesAndroidInjector
    @ActivityScope
    internal abstract fun bindHomeActivity(): HomeActivity
}
