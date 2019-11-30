package com.natasha.clockio.base.model

import retrofit2.Response

//https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
@Suppress("unused")
sealed class ApiResponse<T> {
  companion object {
    fun <T> create(error: Throwable): ApiErrorResponse<T> {
      return ApiErrorResponse(error.message ?: "unknown error")
    }

    fun <T> create(response: Response<T>): ApiResponse<T> {
      return if (response.isSuccessful) {
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
        ApiErrorResponse(errorMsg?: "unknown error")
      }
    }
  }
}

data class ApiSuccessResponse<T>(val body: T): ApiResponse<T>()

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String): ApiResponse<T>()