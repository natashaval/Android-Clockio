package com.natasha.clockio.location.entity

import java.io.Serializable

data class Location (
    val employeeId: String,
    val latitude: Double,
    val longitude: Double
): Serializable