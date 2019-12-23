package com.natasha.clockio.presence.repository

import android.content.Context
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.natasha.clockio.R
import com.natasha.clockio.base.constant.CloudinaryConst
import com.natasha.clockio.presence.ui.fragment.ImageFragment
import com.natasha.clockio.presence.util.CallbackAggregator
import java.util.*
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(private val context: Context): ImageRepository {

  private val TAG: String = "ImageRepository"

  override fun uploadImage(userId: String, imagePath: String) {
    MediaManager.get().upload(imagePath)
        .unsigned(CloudinaryConst.UNSIGNED)
        .option("public_id", userId)
        .option("folder", CloudinaryConst.UPLOAD_FOLDER)
        .callback(object : UploadCallback {
          override fun onSuccess(
              requestId: String?,
              resultData: MutableMap<Any?, Any?>?
          ) {
            Log.d(TAG, "Upload Cloudinary Success $requestId; result: $resultData")
            // result: {resource_type=image, access_mode=public, etag=0a68980d44ad42d00ee15d0daba3b58f, signature=e9a53e383ca4ee45e7a35eb472e82844a3393ab5, url=http://res.cloudinary.com/jengsusy/image/upload/v1574009226/Android/0a87a882-4439-4501-9b1d-01282b374e4d.jpg, height=5173, secure_url=https://res.cloudinary.com/jengsusy/image/upload/v1574009226/Android/0a87a882-4439-4501-9b1d-01282b374e4d.jpg, existing=false, format=jpg, public_id=Android/0a87a882-4439-4501-9b1d-01282b374e4d, version=1574009226, original_filename=1574009168890, placeholder=false, width=3880, created_at=2019-11-17T16:47:06Z, tags=[], type=upload, bytes=3560202}
          }

          override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
            val progress = bytes / totalBytes
            Log.d(TAG, "Upload Cloudinary Progress $progress")
          }

          override fun onReschedule(requestId: String?, error: ErrorInfo?) {

          }

          override fun onError(requestId: String?, error: ErrorInfo?) {
            Log.e(TAG, "Upload Cloudinary Failed $requestId; error: $error")
          }

          override fun onStart(requestId: String?) {
            Log.d(TAG, "Upload Cloudinary Started $requestId")
          }

        })
        .dispatch()
  }

  override fun uploadImageCallback(userId: String, imagePath: String,
      callback: ImageRepository.UploadEvents) {

    val callbackAggregator = CallbackAggregator(callback, imagePath)
    MediaManager.get()
        .upload(imagePath)
        .unsigned(CloudinaryConst.UNSIGNED)
        .option("public_id", userId)
        .option("folder", CloudinaryConst.UPLOAD_FOLDER)
        .callback(callbackAggregator)
        .dispatch()
  }

}