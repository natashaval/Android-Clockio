package com.natasha.clockio.login.data

import android.util.Log
import com.natasha.clockio.base.constant.UrlConst
import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.base.util.ResponseUtils
import com.natasha.clockio.login.service.AuthApi
import retrofit2.Call
import javax.inject.Inject

class LoginRepository @Inject constructor(private val authApi: AuthApi) {

    private val TAG: String = LoginRepository::class.java.simpleName
    fun login(username: String, password: String): Call<AccessToken> {
        return authApi.requestToken(username, password, UrlConst.BASIC_GRANT)
    }

    suspend fun getProfile(): BaseResponse<Any> {
        val response = authApi.getProfile()
        Log.d(TAG, "login repository getProfile $response")
        return ResponseUtils.convertResponse(response)
    }

}
