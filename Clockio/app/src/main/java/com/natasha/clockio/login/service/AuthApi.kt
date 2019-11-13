package com.natasha.clockio.login.service

import com.natasha.clockio.base.model.AccessToken
import com.natasha.clockio.base.model.LoggedInUser
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

interface AuthApi {
    //https://medium.com/skyshidigital/kotlin-coroutines-solusi-asynchronous-di-android-ee4c83e5f962
    // kayaknya hanya GET yang di suspend

    /*
    Sebagai penutup, Coroutines sangat cocok digunakan untuk melakukan asynchronous programming dengan kondisi saat fetch API, read-write database, parsing data, dan kondisi dimana hanya membutuhkan satu pekerjaan ditiap function yang mungkin dapat menggangu UI thread pada Aplikasi android.
     */

    /*@FormUrlEncoded
    @POST("/oauth/token")
    fun requestToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grant: String
    ): Call<AccessToken>*/
    @FormUrlEncoded
    @POST("/oauth/token")
    suspend fun requestToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grant: String
    ): Response<AccessToken>

    @FormUrlEncoded
    @POST("/oauth/token")
    suspend fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grant: String
    ): Call<AccessToken>

    //    https://proandroiddev.com/suspend-what-youre-doing-retrofit-has-now-coroutines-support-c65bd09ba067
    @GET("/api/profile")
    suspend fun getProfile(
    ): Response<LoggedInUser>
}