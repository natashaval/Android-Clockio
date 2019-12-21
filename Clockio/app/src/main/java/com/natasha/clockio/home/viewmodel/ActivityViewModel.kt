package com.natasha.clockio.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasha.clockio.activity.entity.ActivityCreateRequest
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.home.repository.ActivityRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ActivityViewModel @Inject constructor(private val activityRepository: ActivityRepository): ViewModel() {
  private val TAG: String = ActivityViewModel::class.java.simpleName

  private val _activity = MutableLiveData<BaseResponse<Any>>()
  val activityToday: LiveData<BaseResponse<Any>>
    get() = _activity

  private val _activityResult = MutableLiveData<BaseResponse<Any>>()
  val activityResult: LiveData<BaseResponse<Any>>
    get() = _activityResult

  fun getActivityToday(id: String) {
    viewModelScope.launch {
//      val queryDate = "2019-12-12 01:01:00"
//      val qDate: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(queryDate)
      val qDate = Date()
      val formatter = SimpleDateFormat("yyyy-MM-dd")
      val response = activityRepository.getActivityToday(id, formatter.format(qDate))
      _activity.value = response
    }
  }

  fun createActivity(id: String, request: ActivityCreateRequest) {
    viewModelScope.launch {
      _activityResult.value = BaseResponse.loading(null)
      val response = activityRepository.createActivity(id, request)
      _activityResult.value = response
    }
  }
}