package com.natasha.clockio.presence.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.natasha.clockio.presence.repository.ImageRepository
import javax.inject.Inject

class ImageViewModel @Inject constructor(private val imageRepository: ImageRepository) : ViewModel() {
  private val TAG: String = ImageViewModel::class.java.simpleName

  private val _progressValue = MutableLiveData<Double>()
  val progressValue: LiveData<Double>
    get() = _progressValue

  private val _events = MutableLiveData<Int>()
  val events: LiveData<Int>
    get() = _events

  companion object {
    val STATUS_CODE_STARTING = 15
    val STATUS_CODE_UPLOAD_ERROR = 16
    val STATUS_CODE_FINISHED = 17
    val STATUS_CODE_PROGRESS = 18
  }

  fun uploadImage(userId: String, imagePath: String) {
    _events.value = STATUS_CODE_STARTING
    imageRepository.uploadImageCallback(userId, imagePath, object: ImageRepository.UploadEvents {
      override fun onStart() {
        _progressValue.value = 0.0
      }

      override fun onError() {
        _events.value = STATUS_CODE_UPLOAD_ERROR
      }

      override fun onFinished(resultData: MutableMap<Any?, Any?>?) {
        _events.value = STATUS_CODE_FINISHED
        _progressValue.value = 1.0
        Log.d(TAG, "Result in ViewModel $resultData")
      }

      override fun onProgress(progress: Double) {
        _events.value = STATUS_CODE_PROGRESS
        _progressValue.value = progress
      }

    })
  }
}
