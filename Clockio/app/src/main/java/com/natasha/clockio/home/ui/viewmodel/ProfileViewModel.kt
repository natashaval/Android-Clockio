package com.natasha.clockio.home.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.home.entity.Employee
import com.natasha.clockio.home.repository.EmployeeRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val employeeRepository: EmployeeRepository): ViewModel() {
  private val TAG: String = ProfileViewModel::class.java.simpleName

  private val _id = MutableLiveData<String>()
  val id: LiveData<String>
  get() = _id


  fun setId(id: String?) {
    if (_id.value != id) {
      _id.value = id
    }
  }
}
