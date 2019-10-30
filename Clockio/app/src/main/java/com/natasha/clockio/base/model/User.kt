package com.natasha.clockio.base.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

//@Entity(tableName = "user")
data class User (
    val id: String,
    @SerializedName("username")
    val username: String,
    val role: Role

) : Serializable

data class Role (val id: String, val role: String) : Serializable