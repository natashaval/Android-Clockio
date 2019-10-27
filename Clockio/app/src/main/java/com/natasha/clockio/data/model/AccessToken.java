package com.natasha.clockio.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class AccessToken {
    @SerializedName(value = "access_token")
    private String accessToken;

    @SerializedName(value = "token_type")
    private String tokenType;

    @SerializedName(value = "refresh_token")
    private String refreshToken;

    @SerializedName(value = "expires_in")
    private int expiresIn;

    private String scope;
    private String jti;
}