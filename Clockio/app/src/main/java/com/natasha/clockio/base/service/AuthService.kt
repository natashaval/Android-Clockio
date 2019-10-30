package com.natasha.clockio.base.service

import com.natasha.clockio.base.model.AccessToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded

interface AuthService {
    @FormUrlEncoded
    @POST("/oauth/token")
    fun requestToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grant: String
    ): Call<AccessToken>

    @FormUrlEncoded
    @POST("/oauth/token")
    fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grant: String
    ): Call<AccessToken>
}