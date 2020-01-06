package com.natasha.clockio.notification.entity

import java.io.Serializable
import java.util.*

data class NotifRequest(
    val title: String,
    val content: String,
    val startDate: Date?,
    val endDate: Date?,
    val latitude: Double?,
    val longitude: Double?
): Serializable