package com.natasha.clockio.login.data

import android.util.Log
import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.model.BaseResponse
import okhttp3.ResponseBody
import java.util.*
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor(private val dataSource: LoginDataSource) {

    private val TAG: String = LoginRepository::class.java.simpleName

    suspend fun login(username: String, password: String,
                      onSuccess: (accessToken: BaseResponse<AccessToken>) -> Unit,
                      onFailed: (errorBody: ResponseBody) -> Unit,
                      onFailure: (t: Throwable) -> Unit) {
        try {
            val response = dataSource.login(username, password)
            if (response.isSuccessful) {
                Log.d(TAG, "login is success " + response.body().toString())
                response.body()?.let { token ->
                    onSuccess.invoke(BaseResponse.success(token))
                }
            }
            else {
                Log.d(TAG, "login on response but failed ${response.code()} errorBody: ${response.errorBody()} msg: ${response.message()}")
                response.errorBody().let { err -> onFailed.invoke(err!!) }
            }
        } catch (t: Throwable) {
            t.message.let { _ ->
                Log.d(TAG, "login is failed")
                onFailure.invoke(t)
            }
        }
    }

    suspend fun getProfile(): BaseResponse<Any> {
        val response = dataSource.profile()
        return try {
            if (response.isSuccessful) {
                Log.d(TAG, "profileSuccess ${response.body()}")
                BaseResponse.success(response.body())
            } else {
                Log.d(TAG, "profileFailed ${response.errorBody()}")
                BaseResponse.failed(response.errorBody())
            }
        }catch (t: Throwable) {
            Log.d(TAG, "profileError ${t.message}")
            BaseResponse.error(t.message, null)
        }
    }

}
