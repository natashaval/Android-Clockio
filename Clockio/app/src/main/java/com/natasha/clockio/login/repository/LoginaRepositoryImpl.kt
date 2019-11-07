package com.natasha.clockio.login.repository

import android.util.Log
import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.login.service.AuthApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginaRepositoryImpl(private val authApi: AuthApi): LoginaRepository {
    override fun login(
        username: String,
        password: String,
        onSuccess: (accessToken: com.natasha.clockio.base.model.Response<AccessToken>) -> Unit,
        onFailed: (errorBody: ResponseBody) -> Unit,
        onFailure: (t: Throwable) -> Unit
    ) {
        Log.d("LoginRepository", "login is called from repository")
//        https://stackoverflow.com/questions/37141013/how-to-return-value-using-async-retrofit-2-0
        authApi.requestToken(username, password, "password").enqueue(object : Callback<AccessToken> {
            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
//                t.message.let { message ->
                Log.d("LoginRepository", "login is failed ${t.message}")
                    onFailure.invoke(t)
//                }
            }

            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                if (response.isSuccessful) {
                    Log.d("LoginRepository", "login is success " + response.body().toString())
                    response.body()?.let { token ->
                        onSuccess.invoke(com.natasha.clockio.base.model.Response.success(token))
                    }
                }
                else {
                    Log.d("LoginRepository", "login on response but failed ${response.code()} errorBody: ${response.errorBody()} msg: ${response.message()}")
                    response.errorBody().let { err -> onFailed.invoke(err!!) }
                }
            }
        })
    }
}