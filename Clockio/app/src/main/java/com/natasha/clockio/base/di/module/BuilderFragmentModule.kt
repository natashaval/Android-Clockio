package com.natasha.clockio.base.di.module

import com.natasha.clockio.activity.ui.ActivityAddFragment
import com.natasha.clockio.base.di.module.repository.ActivityModule
import com.natasha.clockio.base.di.module.repository.ImageModule
import com.natasha.clockio.base.di.module.repository.NotifModule
import com.natasha.clockio.base.di.module.repository.ProfileModule
import com.natasha.clockio.base.di.module.viewmodel.*
import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.activity.ui.ActivityFragment
import com.natasha.clockio.activity.ui.ActivityHistoryFragment
import com.natasha.clockio.friend.ui.EmployeeAddFragment
import com.natasha.clockio.home.ui.fragment.DashboardAdminFragment
import com.natasha.clockio.home.ui.fragment.FriendFragment
import com.natasha.clockio.home.ui.fragment.ProfileFragment
import com.natasha.clockio.notification.ui.NotifAddFragment
import com.natasha.clockio.notification.ui.NotifFragment
import com.natasha.clockio.presence.ui.fragment.ImageFragment
import com.natasha.clockio.presence.ui.fragment.LockFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

//https://dev.to/autonomousapps/the-daggerandroid-missing-documentation-part-3-fragments-12go
@Module
abstract class BuilderFragmentModule {
  @ContributesAndroidInjector(modules = [
    PresenceViewModelModule::class,
    LocationViewModelModule::class,
    ImageModule::class])
  @ActivityScope
  abstract fun imageFragment(): ImageFragment

  @ContributesAndroidInjector(modules = [
    ProfileViewModelModule::class,
    LocationViewModelModule::class,
    ProfileModule::class])
  @ActivityScope
  abstract fun profileFragment(): ProfileFragment

  @ContributesAndroidInjector(modules = [
    ProfileViewModelModule::class, ActivityViewModelModule::class,
    ProfileModule::class, ActivityModule::class])
  @ActivityScope
  abstract fun activityFragment(): ActivityFragment

  @ContributesAndroidInjector(modules = [
  ActivityViewModelModule::class, ActivityModule::class, LocationViewModelModule::class])
  @ActivityScope
  abstract fun activityAddFragment(): ActivityAddFragment

  @ContributesAndroidInjector(modules = [
    ActivityViewModelModule::class, ActivityModule::class])
  @ActivityScope
  abstract fun activityHistoryFragment(): ActivityHistoryFragment

  @ContributesAndroidInjector(modules = [
    DashboardViewModelModule::class])
  @ActivityScope
  abstract fun activityDashboardAdminFragment(): DashboardAdminFragment

  @ContributesAndroidInjector
  @ActivityScope
  abstract fun lockFragment(): LockFragment

  @ContributesAndroidInjector(modules = [
  NotifViewModelModule::class, NotifModule::class])
  @ActivityScope
  abstract fun notifFragment(): NotifFragment

  @ContributesAndroidInjector(modules = [
    NotifViewModelModule::class, NotifModule::class])
  @ActivityScope
  abstract fun notifAddFragment(): NotifAddFragment

  @ContributesAndroidInjector(modules = [FriendViewModelModule::class])
  @ActivityScope
  abstract fun friendFragment(): FriendFragment

  @ContributesAndroidInjector(modules = [ProfileViewModelModule::class, ProfileModule::class])
  @ActivityScope
  abstract fun employeeAddFragment(): EmployeeAddFragment
}