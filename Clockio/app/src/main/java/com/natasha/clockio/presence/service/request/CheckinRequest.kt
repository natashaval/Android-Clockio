package com.natasha.clockio.presence.service.request

import java.io.Serializable
import java.util.*

data class CheckinRequest(
    val employeeId: String,
    val url: String?,
    val checkIn: Date?,
    val latitude: Double,
    val longitude: Double
) : Serializable