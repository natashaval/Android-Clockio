package com.natasha.clockio.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Role(
        @field:SerializedName("id")
        val id: Long,

        @field:SerializedName("role")
        val role: String

) : Parcelable