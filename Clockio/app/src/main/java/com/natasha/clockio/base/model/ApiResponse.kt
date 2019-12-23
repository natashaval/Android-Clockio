package com.natasha.clockio.base.model

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Response

//https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
@Suppress("unused")
sealed class ApiResponse<T> {
  companion object {
    fun <T> create(error: Throwable): ApiErrorResponse<T> {
      return ApiErrorResponse(error.message ?: "unknown error")
    }

    fun <T> create(response: Response<T>): ApiResponse<T> {
      return try {
        if (response.isSuccessful) {
          val body = response.body()
          if (body == null) {
            ApiEmptyResponse()
          } else {
            ApiSuccessResponse(body)
          }
        } else {
          /*val msg = response.errorBody()?.toString()
          val errorMsg = if (msg.isNullOrEmpty()) {
            response.message()
          } else {
            msg
          }
          ApiFailedResponse(errorMsg ?: "unknown error")*/

          val errBody = response.errorBody()
          if (errBody == null) {
            ApiEmptyResponse()
          } else {
//            Log.d("ApiResponse", "api errBody ${errBody.string()}")
            ApiFailedResponse(errBody)
          }
        }
      } catch (t: Throwable) {
        ApiErrorResponse(t.message ?: "throwable error")
      }
    }
  }
}

data class ApiSuccessResponse<T>(val body: T): ApiResponse<T>()

class ApiEmptyResponse<T> : ApiResponse<T>()

//data class ApiFailedResponse<T>(val errorMessage: String): ApiResponse<T>()
data class ApiFailedResponse<T>(val errorBody: ResponseBody): ApiResponse<T>()

data class ApiErrorResponse<T>(val t: String): ApiResponse<T>()