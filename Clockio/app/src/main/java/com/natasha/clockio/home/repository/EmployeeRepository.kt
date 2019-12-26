package com.natasha.clockio.home.repository

import android.util.Log
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.base.util.ResponseUtils
import com.natasha.clockio.home.dao.EmployeeDao
import com.natasha.clockio.home.service.EmployeeApi
import javax.inject.Inject

class EmployeeRepository @Inject constructor(private val employeeApi: EmployeeApi) {
  private val TAG: String = EmployeeRepository::class.java.simpleName

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

  suspend fun updateStatus(id: String, status: String): BaseResponse<Any> {
    val response = employeeApi.updateStatus(id, status)
    Log.d(TAG, "update status $response")
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