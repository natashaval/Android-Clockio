package com.natasha.clockio.home.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
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
    val updatedBy: String?

    //    @Ignore
    //    val department: Department
) {
  data class Department(
      val id: String?,
      val name: String,
      val branchId: String?
  )
}