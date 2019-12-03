package com.natasha.clockio.home.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.natasha.clockio.base.database.AppDatabase
import com.natasha.clockio.base.di.executor.AppExecutors
import com.natasha.clockio.base.model.ApiResponse
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.base.util.NetworkBoundResource
import com.natasha.clockio.home.dao.EmployeeDao
import com.natasha.clockio.home.entity.Employee
import com.natasha.clockio.home.service.EmployeeApi
import javax.inject.Inject

class EmployeeRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val employeeDao: EmployeeDao,
    private val employeeApi: EmployeeApi
) {
  //class EmployeeRepository @Inject constructor(private val database: AppDatabase){
  private val TAG: String = EmployeeRepository::class.java.simpleName
  //  private var employeeDao: EmployeeDao = database.employeeDao()

  fun getEmployeeById(id: String) : LiveData<BaseResponse<Employee>> {
    return object : NetworkBoundResource<Employee, Employee>(appExecutors) {
      override fun loadFromDb(): LiveData<Employee> {
        val result = employeeDao.getById(id)
        Log.d(TAG, "load From DB ${result.value}")
//        return employeeDao.getById(id)
        return result
      }

      override fun shouldFetch(data: Employee?) = data == null

      override fun createCall(): LiveData<ApiResponse<Employee>> {
        Log.d(TAG, "create Call")
        return employeeApi.getEmployeeById(id)
      }

      override fun saveCallResult(item: Employee) {
        Log.d(TAG, "Employee save Call Result")
        employeeDao.insert(item)
      }
    }.asLiveData()
  }
}