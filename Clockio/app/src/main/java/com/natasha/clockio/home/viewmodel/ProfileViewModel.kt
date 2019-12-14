package com.natasha.clockio.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.home.entity.Employee
import com.natasha.clockio.home.repository.ProfileRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository): ViewModel() {
  private val TAG: String = ProfileViewModel::class.java.simpleName

  private val _id = MutableLiveData<String>()
  val id: LiveData<String>
  get() = _id

  val employee: LiveData<BaseResponse<Employee>> = Transformations
      .switchMap(_id) {id ->
        Log.d(TAG, "profileViewModule id $id")
        profileRepository.getEmployeeById(id)
      }

  fun setId(id: String?) {
    if (_id.value != id) {
      _id.value = id
    }
    Log.d(TAG, "profileViewModule _id ${_id.value}")
  }
}
