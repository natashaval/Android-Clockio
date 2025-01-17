package com.natasha.clockio.base.model

import java.io.Serializable

class LoggedInUser(
    val id: String,
    val employeeId: String,
    val username: String,
    val role: Role
) : Serializable

data class Role (val id: Int, val role: String) : Serializable