package com.natasha.clockio.service;

import com.natasha.clockio.model.Test;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LoginService {
    @GET("/about")
    Call<String> getAbout();

    @GET("/test")
    Call<Test> getTestJson();
}
