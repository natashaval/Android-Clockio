package com.natasha.clockio.login.data

import android.util.Log
import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.model.Response
import com.natasha.clockio.login.data.model.LoggedInUser
import com.natasha.clockio.login.service.AuthApi
import java.lang.Exception
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor(private val dataSource: LoginDataSource) {

    private val TAG: String = LoginRepository::class.java.simpleName

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

/*
    fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }*/

    fun login(username: String, password: String): Response<AccessToken> {
        try {
            Log.d(TAG, "login is called from repository")
            val response = dataSource.login(username, password)
//            Log.d(TAG, "Success " + response.body().toString())
//            return Response.success(response)
            return Response.success(null)
        } catch (e: Exception) {
            Log.e(TAG, "Error " + e.message)
            return Response.error(e.message!!, null)
        }
    }
//    suspend fun login(username: String, password: String) = authApi.requestToken(username, password)

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
