package com.natasha.clockio.login.data

import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.login.service.AuthApi
import retrofit2.Response
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(private val authApi: AuthApi) {

    suspend fun login(username: String, password: String): Response<AccessToken> =
        authApi.requestToken(username, password, "password")

    fun logout() {
        // TODO: revoke authentication
    }
}

