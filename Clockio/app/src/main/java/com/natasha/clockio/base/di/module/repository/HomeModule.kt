package com.natasha.clockio.base.di.module.repository

import com.natasha.clockio.base.database.AppDatabase
import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.home.dao.EmployeeDao
import com.natasha.clockio.home.repository.EmployeeRepository
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @Provides
    @ActivityScope
    fun provideEmployeeRepository(employeeDao: EmployeeDao): EmployeeRepository {
        return EmployeeRepository(employeeDao)
    }

    @Provides
    @ActivityScope
    fun provideEmployeeDao(database: AppDatabase) = database.employeeDao()

//    @Provides
//    @ActivityScope
//    fun provideEmployeeRepository(database: AppDatabase): EmployeeRepository {
//        return EmployeeRepository(database)
//    }
}