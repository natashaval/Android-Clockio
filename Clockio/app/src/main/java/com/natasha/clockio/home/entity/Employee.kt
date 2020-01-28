package com.natasha.clockio.home.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "employees",
    primaryKeys = ["id"])
data class Employee(
    var id: String,
    val status: String?,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String?,
    @ColumnInfo val phone: String?,
    @ColumnInfo val email: String?,
    @ColumnInfo(name = "profile_url") val profileUrl: String?,

    @SerializedName("lastLatitude")
    @ColumnInfo val latitude: Double?,
    @SerializedName("lastLongitude")
    @ColumnInfo val longitude: Double?,
    @SerializedName("lastCheckIn")
    @ColumnInfo val checkIn: Date?,
    @SerializedName("lastCheckOut")
    @ColumnInfo val checkOut: Date?,

    val createdAt: Date?,
    val createdBy: String?,
    val updatedAt: Date?,
    val updatedBy: String?,

    @Embedded(prefix = "dept_")
    val department: Department
) {
  constructor(): this(
      "", "", "", "", "", "", "", 0.0, 0.0,
      null,null,null,null,null,null, Department()
  )
}