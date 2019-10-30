package com.natasha.clockio.base.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AccessToken (
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName(value = "token_type")
    val tokenType: String,

    @SerializedName(value = "refresh_token")
    val refreshToken: String,

    @SerializedName(value = "expires_in")
    val expiresIn: Int,

    val scope: String,
    val jti: String

) : Serializable