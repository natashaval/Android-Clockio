package com.natasha.clockio.home.repository

import android.util.Log
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.base.model.PageResponse
import com.natasha.clockio.base.util.ResponseUtils
import com.natasha.clockio.home.entity.Employee
import com.natasha.clockio.home.entity.EmployeeRequest
import com.natasha.clockio.home.entity.UserRequest
import com.natasha.clockio.home.service.EmployeeApi
import com.natasha.clockio.login.service.AuthApi
import javax.inject.Inject

class EmployeeRepository @Inject constructor(private val employeeApi: EmployeeApi, private val authApi: AuthApi) {
  private val TAG: String = EmployeeRepository::class.java.simpleName

  /*suspend fun getEmployee(id: String) : BaseResponse<Any> {
    val response = employeeApi.getEmployee(id)
    return ResponseUtils.convertResponse(response)
  }*/

  suspend fun updateStatus(id: String, status: String): BaseResponse<Any> {
    val response = employeeApi.updateStatus(id, status)
    Log.d(TAG, "update status $response")
    return ResponseUtils.convertResponse(response)
  }

  suspend fun getDepartments() : BaseResponse<Any> {
    val response = employeeApi.getDepartments()
    return ResponseUtils.convertResponse(response)
  }

  suspend fun findAllEmployees(page: Int?, size: Int?,
      onSuccess: (empList: PageResponse<Employee>) -> Unit,
      onError: (error: String) -> Unit) {
    val response = employeeApi.findAllEmployees(page, size)
    try {
      if (response.isSuccessful) {
        response.body()?.let { onSuccess(it) }
      } else {
        onError(response.errorBody()?.toString() ?: "unkown error")
      }
    } catch (t: Throwable) {
      Log.d(TAG, "emp GET ALL failed ${t.message}")
      onError(t.message?: "unknown error")
    }
  }

  suspend fun createUser(userRequest: UserRequest, employeeRequest: EmployeeRequest): BaseResponse<Any> {
    val userResponse = authApi.createUser(userRequest)
    try {
        if (userResponse.isSuccessful) {
          Log.d(TAG, "user response success")
          userResponse.body()?.let {
            return createEmployee(it.id, employeeRequest)
          }
        }
    } catch (t: Throwable) {
      t.printStackTrace()
      return BaseResponse.error(t.message, null)
    }
    return BaseResponse.error("unknown error", null)
  }

  suspend fun createEmployee(userId: String, employeeRequest: EmployeeRequest): BaseResponse<Any> {
    employeeRequest.userId = userId
    val response = employeeApi.createEmployee(employeeRequest)
    return ResponseUtils.convertResponse(response)
  }
}