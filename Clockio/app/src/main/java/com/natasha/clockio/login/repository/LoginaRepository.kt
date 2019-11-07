package com.natasha.clockio.login.repository

import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.model.Response
import okhttp3.ResponseBody

interface LoginaRepository {
    fun login(username: String, password: String,
              onSuccess: (accessToken: Response<AccessToken>) -> Unit,
              onFailed: (errorBody: ResponseBody) -> Unit,
              onFailure: (t: Throwable) -> Unit)
}