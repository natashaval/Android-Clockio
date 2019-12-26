package com.natasha.clockio.login.data

import android.util.Log
import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.model.BaseResponse
import retrofit2.Call
import javax.inject.Inject

class LoginRepository @Inject constructor(private val dataSource: LoginDataSource) {

    private val TAG: String = LoginRepository::class.java.simpleName
    fun login(username: String, password: String): Call<AccessToken> {
        return dataSource.login(username, password)
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
