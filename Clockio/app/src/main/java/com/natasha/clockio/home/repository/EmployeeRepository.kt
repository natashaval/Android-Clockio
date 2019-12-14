package com.natasha.clockio.home.repository

import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.home.service.EmployeeApi
import javax.inject.Inject

class EmployeeRepository @Inject constructor(private val employeeApi: EmployeeApi) {
  suspend fun getEmployee(id: String) : BaseResponse<Any> {
    val response = employeeApi.getEmployee(id)
    return try {
      if (response.isSuccessful) {
        BaseResponse.success(response.body())
      } else {
        BaseResponse.failed(response.errorBody())
      }
    } catch (t: Throwable) {
      t.printStackTrace()
      BaseResponse.error(t.message, null)
    }
  }
}