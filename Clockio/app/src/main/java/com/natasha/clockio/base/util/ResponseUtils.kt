package com.natasha.clockio.base.util

import com.natasha.clockio.base.model.BaseResponse
import retrofit2.Response

object ResponseUtils {
  fun <T> convertResponse(response: Response<in T>) : BaseResponse<Any> {
    return try {
      if (response.isSuccessful) {
        BaseResponse.success(response.body())
      } else {
        BaseResponse.failed(response.errorBody())
      }
    } catch (t: Throwable) {
      t.printStackTrace()
      BaseResponse.error(t.message, null)
    }
  }
}