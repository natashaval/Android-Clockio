package com.natasha.clockio.home.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Activity (
    val id: String,
    val title: String,
    val content: String?,
    val date: Date,
    val startTime: String?,
    val endTime: String?
): Parcelable