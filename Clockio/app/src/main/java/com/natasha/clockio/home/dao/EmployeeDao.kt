package com.natasha.clockio.home.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.natasha.clockio.home.entity.Employee
import java.util.*

@Dao interface EmployeeDao {

  @Query("SELECT * FROM employees") fun getAll(): List<Employee>

  @Query("SELECT * FROM employees WHERE id = :id LIMIT 1") fun getById(id: String): Employee

  @Insert fun insertAll(vararg employee: Employee)
}