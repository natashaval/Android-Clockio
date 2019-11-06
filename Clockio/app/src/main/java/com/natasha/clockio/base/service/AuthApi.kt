package com.natasha.clockio.login.service

import com.natasha.clockio.base.model.Test
import com.natasha.clockio.login.data.model.AccessToken
import com.natasha.clockio.login.data.model.LoggedInUser
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

interface AuthApi {
    @FormUrlEncoded
    @POST("/oauth/token")
    fun requestToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grant: String = "password"
    ): Call<AccessToken>

    @FormUrlEncoded
    @POST("/oauth/token")
    fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grant: String
    ): Call<AccessToken>

//    https://proandroiddev.com/suspend-what-youre-doing-retrofit-has-now-coroutines-support-c65bd09ba067
    @GET("/api/profile")
    suspend fun getProfile() : Response<LoggedInUser>
}