package com.natasha.clockio.presence.service.request

import java.util.*

data class CheckoutRequest(
    val checkIn: Date,
    val latitude: Double?,
    val longitude: Double?
)