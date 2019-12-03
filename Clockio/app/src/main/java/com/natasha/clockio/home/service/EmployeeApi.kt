package com.natasha.clockio.home.service

import androidx.lifecycle.LiveData
import com.natasha.clockio.base.model.ApiResponse
import com.natasha.clockio.home.entity.Employee
import retrofit2.http.GET
import retrofit2.http.Path

interface EmployeeApi {
  @GET("/api/employees/{id}")
  fun getEmployeeById(
      @Path("id") id: String
  ) : LiveData<ApiResponse<Employee>>
}