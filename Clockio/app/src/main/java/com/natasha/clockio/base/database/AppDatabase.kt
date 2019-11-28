package com.natasha.clockio.base.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.natasha.clockio.home.dao.Converters
import com.natasha.clockio.home.dao.EmployeeDao
import com.natasha.clockio.home.entity.Employee

//https://proandroiddev.com/android-architecture-starring-kotlin-coroutines-jetpack-mvvm-room-paging-retrofit-and-dagger-7749b2bae5f7
@Database(entities = [Employee::class], version = 1, exportSchema = false)
//https://developer.android.com/training/data-storage/room/referencing-data (for date)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun employeeDao(): EmployeeDao
}