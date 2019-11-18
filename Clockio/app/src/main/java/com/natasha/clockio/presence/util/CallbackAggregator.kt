package com.natasha.clockio.presence.util

import android.util.Log
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.natasha.clockio.presence.repository.ImageRepository
import java.util.concurrent.ConcurrentHashMap

class CallbackAggregator: UploadCallback {

  private val TAG: String = CallbackAggregator::class.java.simpleName

  private lateinit var callback: ImageRepository.UploadEvents
  private lateinit var imagePath: String
  private var started: Boolean = false

  constructor(callback: ImageRepository.UploadEvents, imagePath: String) {
    this.callback = callback
    this.imagePath = imagePath
  }

  override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
    Log.d(TAG, "Callback onSuccess $requestId; result $resultData")
    callback.onFinished(resultData)
  }

  override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
    val progress: Double = bytes.toDouble() / totalBytes
    Log.d(TAG, "Callback onProgress $requestId; progress $progress")
    callback.onProgress(progress)
  }

  override fun onReschedule(requestId: String?, error: ErrorInfo?) {
    Log.e(TAG, "Callback onReschedule $requestId; error $error")
    callback.onError()
  }

  override fun onError(requestId: String?, error: ErrorInfo?) {
    Log.e(TAG, "Callback onError $requestId; error $error")
    callback.onError()
  }

  override fun onStart(requestId: String?) {
    if (!started) {
      started = true
      Log.d(TAG, "Callback onStart $requestId; imagePath $imagePath")
      callback.onStart()
    }
  }

}