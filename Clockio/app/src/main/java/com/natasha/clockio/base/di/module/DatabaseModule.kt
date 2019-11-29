package com.natasha.clockio.base.di.module

import android.content.Context
import androidx.room.Room
import com.natasha.clockio.base.database.AppDatabase
import com.natasha.clockio.base.di.scope.ActivityScope
import com.natasha.clockio.base.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
  @Provides
  @ApplicationScope
  fun provideDb(context: Context): AppDatabase =
      Room.databaseBuilder(context, AppDatabase::class.java, "clockio-db")
          .fallbackToDestructiveMigration()
          .build()

/*    @Provides
    @ActivityScope
    fun provideEmployeeDao(database: AppDatabase) = database.employeeDao()*/
}