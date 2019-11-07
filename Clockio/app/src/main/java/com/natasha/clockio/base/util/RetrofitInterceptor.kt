package com.natasha.clockio.base.util

import android.text.TextUtils
import android.util.Log
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

import javax.inject.Inject
import javax.inject.Singleton
import java.io.IOException

//https://jakewharton.com/making-retrofit-work-for-you/
@Singleton
class RetrofitInterceptor @Inject constructor() : Interceptor {

    private var token = ""
    private var clientId: String? = null
    private var clientSecret: String? = null

    fun setToken(token: String) {
        this.token = token
    }

    fun setBasic(clientId: String, clientSecret: String) {
        this.clientId = clientId
        this.clientSecret = clientSecret
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (request.url.encodedPath == "/oauth/token") {
            val basic = Credentials.basic(clientId!!, clientSecret!!)
            request = request.newBuilder()
                .addHeader("Authorization", basic)
                .addHeader("Content-Type", "application/json")
                .build()
            Log.d(TAG, "retrofit add header basic $basic")
        } else if (!TextUtils.isEmpty(token)) {
            Log.d(TAG, "retrofit add header token")
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json")
                .build()
        } else {
            Log.d(TAG, "retrofit add header NOTHING!")
        }
        return chain.proceed(request)
    }

    companion object {
        private val TAG = RetrofitInterceptor::class.java.simpleName
    }
}
