package com.natasha.clockio.presence.service.request

import java.util.*

data class CheckinResult(
    var id: String,
    var photo: String?,
    var checkIn: Date?,
    var checkOut: Date?,
    var latitude: Double?,
    var longitude: Double?
)