package com.natasha.clockio.base.util

import android.content.Context
import android.util.Log
import com.natasha.clockio.activity.ui.ActivityAddFragment
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.base.model.DataResponse
import com.natasha.clockio.base.ui.alertError
import com.natasha.clockio.base.ui.alertFailed
import com.natasha.clockio.base.ui.alertSaved
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

  fun showResponseAlert(context: Context, it: BaseResponse<Any>) {
    when(it.status) {
      BaseResponse.Status.SUCCESS -> {
        it.data?.let {result ->
          Log.d(ActivityAddFragment.TAG, "create something success $result")
          val response = result as DataResponse
          alertSaved(context, response.message)
        }
      }
      BaseResponse.Status.FAILED -> {
        it.data?.let {result->
          Log.d(ActivityAddFragment.TAG, "create something failed $result")
          val response = result as DataResponse
          alertFailed(context, response.message)
        }
      }
      BaseResponse.Status.ERROR -> {
        Log.d(ActivityAddFragment.TAG, "create something error ${it.data}")
        alertError(context, it.data.toString())
      }
      else -> {
        Log.d(ActivityAddFragment.TAG, "something do nothing")
      }
    }
  }
}