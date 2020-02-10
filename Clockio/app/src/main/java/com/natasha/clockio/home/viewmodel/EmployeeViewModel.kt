package com.natasha.clockio.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.home.entity.Department
import com.natasha.clockio.home.entity.EmployeeRequest
import com.natasha.clockio.home.entity.UserRequest
import com.natasha.clockio.home.repository.EmployeeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmployeeViewModel @Inject constructor(private val employeeRepository: EmployeeRepository) : ViewModel() {
  private val TAG: String = EmployeeViewModel::class.java.simpleName

  /*private val _employee = MutableLiveData<BaseResponse<Any>>()
  val employee: LiveData<BaseResponse<Any>>
  get() = _employee*/

  private val _departmentList = MutableLiveData<List<Department>>()
  val departmentList: LiveData<List<Department>>
  get() = _departmentList

  private val _employeeCreate = MutableLiveData<BaseResponse<Any>>()
  val employeeCreate: LiveData<BaseResponse<Any>>
    get() = _employeeCreate

  /*fun getEmployee(id: String) {
    viewModelScope.launch {
      _employee.value = BaseResponse.loading(null)
      val response = employeeRepository.getEmployee(id)
      Log.d(TAG, "employee get $response")
      _employee.value = response
    }
  }*/

  fun updateStatus(id: String, status: String) {
    viewModelScope.launch {
      val response = employeeRepository.updateStatus(id, status)
//      _employee.value = response
      when(response.status) {
        BaseResponse.Status.SUCCESS -> {
          Log.d(TAG, "status ${response.data}")
        }
      }
    }
  }

  fun getDepartment() {
    viewModelScope.launch {
      val response = employeeRepository.getDepartments()
      when (response.status) {
        BaseResponse.Status.SUCCESS -> {
          _departmentList.value = response.data as List<Department>
        }
      }
    }
  }

  fun createUser(userRequest: UserRequest, employeeRequest: EmployeeRequest) {
    viewModelScope.launch {
      val response = employeeRepository.createUser(userRequest, employeeRequest)
      Log.d(TAG,"create Employee $response")
      _employeeCreate.value = response
    }
  }
}
