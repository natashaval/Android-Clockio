package com.natasha.clockio.base.util

import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import com.natasha.clockio.base.constant.PreferenceConst
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

import javax.inject.Inject
import javax.inject.Singleton
import java.io.IOException

//https://jakewharton.com/making-retrofit-work-for-you/
@Singleton
class RetrofitInterceptor @Inject constructor(private val sharedPref: SharedPreferences) : Interceptor {

    private var token = ""
    private var clientId: String? = null
    private var clientSecret: String? = null
    private var existToken = sharedPref.getString(PreferenceConst.ACCESS_TOKEN_KEY, "")

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
        } else if (!TextUtils.isEmpty(existToken)) {
            Log.d(TAG, "retrofit add header token from Shared Pref $existToken")
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $existToken")
                .addHeader("Content-Type", "application/json")
                .build()
        } else if (!TextUtils.isEmpty(token)) {
            Log.d(TAG, "retrofit add header token from Login $token")
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
