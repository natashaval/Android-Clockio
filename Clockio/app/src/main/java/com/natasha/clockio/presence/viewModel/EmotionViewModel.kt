package com.natasha.clockio.presence.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.presence.service.EmotionApi
import com.natasha.clockio.presence.service.response.EmotionRequest
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmotionViewModel @Inject constructor(private val emotionApi: EmotionApi): ViewModel() {

    private val TAG: String = EmotionViewModel::class.java.simpleName

    private val _emotion = MutableLiveData<BaseResponse<Any>>()
    val emotion: LiveData<BaseResponse<Any>>
        get() = _emotion

    fun getEmotion(attr: String, request: EmotionRequest) {
        viewModelScope.launch {
            _emotion.value = BaseResponse.loading(null)
            val response = emotionApi.detectFace(attr, request)
            try {
                if (response.isSuccessful) {
                    _emotion.value = BaseResponse.success(response.body())
                } else {
                    _emotion.value = BaseResponse.failed(response.errorBody())
                }
            } catch (t: Throwable) {
                _emotion.value = BaseResponse.error(t.message, null)
            }
        }

    }
}