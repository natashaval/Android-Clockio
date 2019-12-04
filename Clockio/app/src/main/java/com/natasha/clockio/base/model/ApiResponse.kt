package com.natasha.clockio.base.model

import retrofit2.Response

//https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
@Suppress("unused")
sealed class ApiResponse<T> {
  companion object {
    fun <T> create(error: Throwable): ApiFailedResponse<T> {
      return ApiFailedResponse(error.message ?: "unknown error")
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
          val msg = response.errorBody()?.toString()
          val errorMsg = if (msg.isNullOrEmpty()) {
            response.message()
          } else {
            msg
          }
          ApiFailedResponse(errorMsg ?: "unknown error")
        }
      } catch (t: Throwable) {
        ApiErrorResponse(t.message ?: "throwable error")
      }
    }
  }
}

data class ApiSuccessResponse<T>(val body: T): ApiResponse<T>()

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiFailedResponse<T>(val errorMessage: String): ApiResponse<T>()

data class ApiErrorResponse<T>(val t: String): ApiResponse<T>()