package com.natasha.clockio.presence.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasha.clockio.base.model.*
import com.natasha.clockio.presence.repository.PresenceRepository
import com.natasha.clockio.presence.service.request.CheckinRequest
import com.natasha.clockio.presence.service.request.CheckoutRequest
import kotlinx.coroutines.launch
import javax.inject.Inject

class PresenceViewModel @Inject constructor(
  private val presenceRepository: PresenceRepository): ViewModel() {
  private val TAG = PresenceViewModel::class.java.simpleName

  private val _presenceCheckin = MutableLiveData<BaseResponse<Any>>()
  val presenceCheckin: LiveData<BaseResponse<Any>>
    get() = _presenceCheckin

  private val _presenceCheckout = MutableLiveData<BaseResponse<Any>>()
  val presenceCheckout: LiveData<BaseResponse<Any>>
    get() = _presenceCheckout

  fun sendCheckIn(employeeId: String, request: CheckinRequest) {
    viewModelScope.launch {
      _presenceCheckin.value = BaseResponse.loading(null)
      val response = presenceRepository.sendCheckIn(employeeId, request)
      _presenceCheckin.value = response
    }
  }

  fun sendCheckOut(presenceId: String, request: CheckoutRequest) {
    viewModelScope.launch {
      _presenceCheckout.value = BaseResponse.loading(null)
      val response = presenceRepository.sendCheckOut(presenceId, request)
      _presenceCheckout.value = response
    }
  }
}
