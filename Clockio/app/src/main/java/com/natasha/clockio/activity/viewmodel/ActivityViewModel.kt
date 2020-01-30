package com.natasha.clockio.activity.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasha.clockio.activity.entity.ActivityCreateRequest
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.base.model.PageResponse
import com.natasha.clockio.home.entity.Activity
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

  private val _activityPage = MutableLiveData<PageResponse<Activity>>()
  val activityPage: LiveData<PageResponse<Activity>>
    get() = _activityPage

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

  fun getActivityHistory(id:String, start: String, end: String, page: Int?, size: Int?) {
    viewModelScope.launch {
      activityRepository.getActivityHistory(id, start, end, page, size, {
        Log.d(TAG, "history Success $it")
        _activityPage.value = it
      }, { err ->
        Log.d(TAG, "history page fetch Failed $err")
      })
    }
  }
}