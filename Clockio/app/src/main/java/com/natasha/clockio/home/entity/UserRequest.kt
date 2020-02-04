package com.natasha.clockio.home.entity

data class UserRequest(
    var username: String,
    var password: String,
    var roleId: Int
)