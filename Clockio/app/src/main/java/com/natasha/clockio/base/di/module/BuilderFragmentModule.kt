package com.natasha.clockio.base.di.module

import com.natasha.clockio.base.di.module.repository.ImageModule
import com.natasha.clockio.base.di.module.viewmodel.ImageViewModelModule
import com.natasha.clockio.base.di.module.viewmodel.LocationViewModelModule
import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.home.ui.fragment.ProfileFragment
import com.natasha.clockio.presence.ui.fragment.ImageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

//https://dev.to/autonomousapps/the-daggerandroid-missing-documentation-part-3-fragments-12go
@Module
abstract class BuilderFragmentModule {
  @ContributesAndroidInjector(modules = [ImageViewModelModule::class, ImageModule::class])
  @ActivityScope
  abstract fun imageFragment(): ImageFragment

  @ContributesAndroidInjector(modules = [LocationViewModelModule::class])
  @ActivityScope
  abstract fun profileFragment(): ProfileFragment
}