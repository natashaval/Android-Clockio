package com.natasha.clockio.home.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class Activity (
    val id: String,
    val title: String,
    val content: String?,
    val date: Date,
    val startTime: String?,
    val endTime: String?
)