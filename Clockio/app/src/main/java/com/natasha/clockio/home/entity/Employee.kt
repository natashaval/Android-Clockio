package com.natasha.clockio.home.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "employees") data class Employee(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "employee_id") var employeeId: String,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo val phone: String?,
    @ColumnInfo val email: String?,
    @ColumnInfo(name = "profile_url") val profileUrl: String?,

    @ColumnInfo val latitude: Double,
    @ColumnInfo val longitude: Double,
    @ColumnInfo val checkIn: Date,
    @ColumnInfo val checkOut: Date)