package com.natasha.clockio.base.model

import java.io.Serializable

class LoggedInUser(
    val id: String,
    val username: String,
    val role: Role
) : Serializable

data class Role (val id: String, val role: String) : Serializable