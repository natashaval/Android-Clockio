package com.natasha.clockio.home.service

import androidx.lifecycle.LiveData
import com.natasha.clockio.base.model.ApiResponse
import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.base.model.PageResponse
import com.natasha.clockio.home.entity.Department
import com.natasha.clockio.home.entity.Employee
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EmployeeApi {
  @GET("/api/employees/{id}")
  fun getEmployeeById(
      @Path("id") id: String
  ) : LiveData<ApiResponse<Employee>>

  @GET("/api/employees/{id}")
  suspend fun getEmployee(@Path("id") id: String)
  : Response<Employee>

  @POST("/api/employees/{id}/status")
  suspend fun updateStatus(@Path("id") id: String, @Query("status") status: String)
  : Response<DataResponse>

  @GET("/api/employees")
  suspend fun findAllEmployees(@Query("page") page: Int?, @Query("size") size: Int?)
  : Response<PageResponse<Employee>>

  @GET("/api/departments")
  suspend fun getDepartments(): Response<List<Department>>
}