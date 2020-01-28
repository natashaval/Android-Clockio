package com.natasha.clockio.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasha.clockio.base.model.PageResponse
import com.natasha.clockio.home.entity.Employee
import com.natasha.clockio.home.repository.EmployeeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendViewModel @Inject constructor(private val employeeRepository: EmployeeRepository) : ViewModel() {
  private val TAG: String = FriendViewModel::class.java.simpleName

  private val _employeePage = MutableLiveData<PageResponse<Employee>>()
  val employeePage: LiveData<PageResponse<Employee>>
    get() = _employeePage

  fun findAllEmployee(page: Int?, size: Int?) {
    viewModelScope.launch {
      employeeRepository.findAllEmployees(page, size, {
        _employeePage.value = it
      }, { err ->
        Log.d(TAG, "employee page fetch Failed $err")
      })
    }
  }
}
