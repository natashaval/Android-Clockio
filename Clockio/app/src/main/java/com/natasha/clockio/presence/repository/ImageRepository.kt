package com.natasha.clockio.presence.repository

// https://cloudinary.com/blog/introducing_the_cloudinary_demo_android_app_part_1
//https://github.com/cloudinary/android-demo
interface ImageRepository {
  fun uploadImageCallback(userId: String, imagePath: String, callback: UploadEvents)

  interface UploadEvents {
    fun onStart()
    fun onError()
    fun onFinished(resultData: MutableMap<Any?, Any?>?)
    fun onProgress(progress: Double)
  }

  fun uploadImage(userId: String, imagePath: String)
}