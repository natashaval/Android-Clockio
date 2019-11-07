package com.natasha.clockio.login.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginaRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String
)
