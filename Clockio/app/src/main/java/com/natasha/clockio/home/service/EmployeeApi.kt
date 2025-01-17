package com.natasha.clockio.home.service

import androidx.lifecycle.LiveData
import com.natasha.clockio.base.model.ApiResponse
import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.base.model.PageResponse
import com.natasha.clockio.home.entity.Department
import com.natasha.clockio.home.entity.Employee
import com.natasha.clockio.home.entity.EmployeeRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface EmployeeApi {
  @GET("/api/employees/{id}")
  fun getEmployeeById(
      @Path("id") id: String
  ) : LiveData<ApiResponse<Employee>>

  @GET("/api/employees/{id}")
  fun getEmployee(@Path("id") id: String)
  : Call<Employee>

  @POST("/api/employees/{id}/status")
  suspend fun updateStatus(@Path("id") id: String, @Query("status") status: String)
  : Response<DataResponse>

  @GET("/api/employees")
  suspend fun findAllEmployees(@Query("page") page: Int?, @Query("size") size: Int?)
  : Response<PageResponse<Employee>>

  @POST("/api/employees")
  suspend fun createEmployee(@Body request: EmployeeRequest): Response<DataResponse>

  @GET("/api/departments")
  suspend fun getDepartments(): Response<List<Department>>
}