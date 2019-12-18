package com.natasha.clockio.base.di.module.repository

import com.natasha.clockio.base.database.AppDatabase
import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.home.repository.EmployeeRepository
import com.natasha.clockio.home.repository.ProfileRepository
import com.natasha.clockio.home.service.EmployeeApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ProfileModule {

  @Provides
  @ActivityScope
  fun provideEmployeeDao(database: AppDatabase) = database.employeeDao()

  @Provides
  @ActivityScope
  fun provideEmployeeRepository(employeeApi: EmployeeApi) = EmployeeRepository(employeeApi)
}