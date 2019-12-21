package com.natasha.clockio.activity.entity

import java.io.Serializable
import java.util.*

data class ActivityCreateRequest (
    val title: String,
    val content: String?,
    val date: Date,
    val startTime: String?,
    val endTime: String?,
    val latitude: Double?,
    val longitude: Double?
): Serializable