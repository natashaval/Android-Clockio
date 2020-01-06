package com.natasha.clockio.notification.ui

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.notification.entity.Notif
import com.natasha.clockio.notification.entity.NotifRequest
import com.natasha.clockio.notification.entity.NotifResult
import com.natasha.clockio.notification.repository.NotifRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//https://blog.mindorks.com/implementing-paging-library-in-android
class NotifViewModel @Inject constructor(private val notifRepository: NotifRepository) : ViewModel() {
  private val TAG: String = NotifViewModel::class.java.simpleName

  private val notifResult: LiveData<NotifResult> = liveData(Dispatchers.IO) {
    val result = notifRepository.getAllNotif()
    Log.d(TAG, "notifResult ${result.data}")
    emit(result)
  }

  val notifs: LiveData<PagedList<Notif>> = Transformations.switchMap(notifResult) {it -> it.data }
  val networkErrors: LiveData<String> = Transformations.switchMap(notifResult) {it -> it.networkErrors }

  private val _notifAddResult = MutableLiveData<BaseResponse<Any>>()
  val notifAddResult: LiveData<BaseResponse<Any>>
    get() = _notifAddResult

  fun createNotif(req: NotifRequest) {
    viewModelScope.launch {
      _notifAddResult.value = BaseResponse.loading(null)
      val response = notifRepository.createNotif(req)
      Log.d(TAG, "notif add view model $response")
      _notifAddResult.value = response
    }

  }
}
