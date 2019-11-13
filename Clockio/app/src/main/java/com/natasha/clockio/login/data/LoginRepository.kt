package com.natasha.clockio.login.data

import android.util.Log
import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.model.BaseResponse
import okhttp3.ResponseBody
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
            t.message.let { message ->
                Log.d(TAG, "login is failed ${t.message}")
                onFailure.invoke(t)
            }
        }
    }

}
