package com.natasha.clockio.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        @field:SerializedName("id")
        val id: String,

        @field:SerializedName("username")
        val username: String,

        @field:SerializedName("role")
        val role: Role
) : Parcelable