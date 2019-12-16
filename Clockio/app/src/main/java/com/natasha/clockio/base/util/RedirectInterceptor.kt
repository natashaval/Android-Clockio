package com.natasha.clockio.base.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.natasha.clockio.login.ui.LoginActivity
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

//https://stackoverflow.com/questions/48340581/retrofit-redirect-to-loginactivity-if-response-code-is-401/49789543
class RedirectInterceptor @Inject constructor(private val context: Context) : Interceptor{

    private val TAG: String = RedirectInterceptor::class.java.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request().newBuilder().build();
        val response: Response = chain.proceed(request)
        Log.d(TAG, "response code ${response.code}")
        if (response.code == 401) {
            response.close()
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            return response
        }
        return response
    }

}