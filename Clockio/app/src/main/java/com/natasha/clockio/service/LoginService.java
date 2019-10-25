package com.natasha.clockio.service;

import com.natasha.clockio.data.model.AccessToken;
import com.natasha.clockio.model.Test;
import com.natasha.clockio.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginService {
    @GET("/about")
    Call<String> getAbout();

    @GET("/test")
    Call<Test> getTestJson();

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> requestToken(
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grant
    );

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> refreshToken(
            @Field("refresh_token") String refreshToken,
            @Field("grant_type") String grant
    );

    @GET("/api/profile")
    Call<User> getProfile();
}
