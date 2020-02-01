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

class DashboardViewModel @Inject constructor(private val employeeRepository: EmployeeRepository): ViewModel() {
  private val TAG: String = DashboardViewModel::class.java.simpleName

  private val _checkinPage = MutableLiveData<PageResponse<Employee>>()
  val checkinPage: LiveData<PageResponse<Employee>>
    get() = _checkinPage

  fun findAllEmployeeByCheckIn(page: Int?, size: Int?) {
    viewModelScope.launch {
      employeeRepository.findAllEmployees(page, size, {
        Log.d(TAG, "checkin page Success $it")
        it.content = it.content?.sortedByDescending { emp -> emp.checkIn }
        _checkinPage.value = it
      }, { err ->
        Log.d(TAG, "checkin page fetch Failed $err")
      })
    }
  }

}
