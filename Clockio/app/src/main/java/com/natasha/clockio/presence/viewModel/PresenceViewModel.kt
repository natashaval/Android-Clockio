package com.natasha.clockio.presence.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.natasha.clockio.base.model.*
import com.natasha.clockio.presence.repository.PresenceRepository
import com.natasha.clockio.presence.service.request.CheckinRequest
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

class PresenceViewModel @Inject constructor(
  private val presenceRepository: PresenceRepository,
  private val gson: Gson): ViewModel() {
  private val TAG = PresenceViewModel::class.java.simpleName

  private val _presenceResult = MutableLiveData<BaseResponse<Any>>()
  val presenceResult: LiveData<BaseResponse<Any>>
    get() = _presenceResult

  fun sendCheckIn(employeeId: String, request: CheckinRequest) {
    viewModelScope.launch {
      _presenceResult.value = BaseResponse.loading(null)
      val result = presenceRepository.sendCheckIn(employeeId, request)
      Log.d(TAG, "checkin $result")
      when (result) {
        is ApiSuccessResponse -> {
          _presenceResult.value = BaseResponse.success(result.body)
        }
        is ApiFailedResponse -> {
          var errBody = gson.fromJson(result.errorBody.string(), DataResponse::class.java)
          _presenceResult.value = BaseResponse.failed(errBody)
        }
        is ApiErrorResponse -> {
          _presenceResult.value = BaseResponse.error(result.t, null)
        }
      }
    }
  }
}
