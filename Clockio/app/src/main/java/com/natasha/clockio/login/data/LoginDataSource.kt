package com.natasha.clockio.login.data

import com.natasha.clockio.base.constant.UrlConst
import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.model.LoggedInUser
import com.natasha.clockio.login.service.AuthApi
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class LoginDataSource @Inject constructor(private val authApi: AuthApi) {

    fun login(username: String, password: String): Call<AccessToken> =
        authApi.requestToken(username, password, UrlConst.BASIC_GRANT)

    suspend fun profile(): Response<LoggedInUser> = authApi.getProfile()
}

