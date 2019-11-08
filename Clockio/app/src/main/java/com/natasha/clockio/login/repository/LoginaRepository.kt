package com.natasha.clockio.login.repository

import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.model.BaseResponse
import okhttp3.ResponseBody

interface LoginaRepository {
    fun login(username: String, password: String,
              onSuccess: (accessToken: BaseResponse<AccessToken>) -> Unit,
              onFailed: (errorBody: ResponseBody) -> Unit,
              onFailure: (t: Throwable) -> Unit)
}