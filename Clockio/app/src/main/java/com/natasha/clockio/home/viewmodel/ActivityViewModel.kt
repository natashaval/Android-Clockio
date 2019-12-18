package com.natasha.clockio.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getActivityToday(id: String) {
        viewModelScope.launch {
            val queryDate = "2019-12-12 01:01:00"
            val qDate: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(queryDate)
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val response = activityRepository.getActivityToday(id, formatter.format(qDate))
            Log.d(TAG, "activityToday $response")
        }
    }
}