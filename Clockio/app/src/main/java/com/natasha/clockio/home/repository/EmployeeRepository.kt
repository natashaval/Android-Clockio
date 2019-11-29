package com.natasha.clockio.home.repository

import com.natasha.clockio.base.database.AppDatabase
import com.natasha.clockio.home.dao.EmployeeDao
import com.natasha.clockio.home.entity.Employee
import javax.inject.Inject

class EmployeeRepository @Inject constructor(private val employeeDao: EmployeeDao) {
//class EmployeeRepository @Inject constructor(private val database: AppDatabase){
  private val TAG: String = EmployeeRepository::class.java.simpleName
//  private var employeeDao: EmployeeDao = database.employeeDao()

  fun getAll() {
    employeeDao.getAll()
  }

  fun getEmployeeById(id: String) {
    employeeDao.getById(id)
  }

  fun saveEmployee(employee: Employee) {
    employeeDao.insert(employee)
  }
}