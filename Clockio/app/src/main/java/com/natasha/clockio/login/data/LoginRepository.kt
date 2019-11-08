package com.natasha.clockio.login.data

import android.util.Log
import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.login.data.model.LoggedInUser
import okhttp3.ResponseBody
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

    /*suspend fun login(username: String, password: String): BaseResponse<AccessToken> {
        try {
            Log.d(TAG, "login is called from repository")
            val response = dataSource.login(username, password)
            Log.d(TAG, "Success " + response.body().toString())
            return BaseResponse.success(response.body())
//            return BaseResponse.success(null)
        } catch (e: Exception) {
            Log.e(TAG, "Error " + e.message)
            return BaseResponse.error(e.message!!, null)
        }
    }*/

    suspend fun login(username: String, password: String,
                      onSuccess: (accessToken: BaseResponse<AccessToken>) -> Unit,
                      onFailed: (errorBody: ResponseBody) -> Unit,
                      onFailure: (t: Throwable) -> Unit) {
        try {
            val response = dataSource.login(username, password)
            if (response.isSuccessful) {
                Log.d("LoginRepository", "login is success " + response.body().toString())
                response.body()?.let { token ->
                    onSuccess.invoke(BaseResponse.success(token))
                }
            }
            else {
                Log.d("LoginRepository", "login on response but failed ${response.code()} errorBody: ${response.errorBody()} msg: ${response.message()}")
                response.errorBody().let { err -> onFailed.invoke(err!!) }
            }
        } catch (t: Throwable) {
            t.message.let { message ->
                Log.d("LoginRepository", "login is failed ${t.message}")
                onFailure.invoke(t)
            }
        }
    }

//    suspend fun login(username: String, password: String) = authApi.requestToken(username, password)

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
