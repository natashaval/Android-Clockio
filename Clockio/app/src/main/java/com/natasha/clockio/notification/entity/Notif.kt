package com.natasha.clockio.notification.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "notif")
data class Notif(
    @PrimaryKey val id: Long,
    val title: String,
    val content: String,
    val latitude: Double?,
    val longitude: Double?,
    val location: String?,
    val startDate: Date?,
    val endDate: Date?,
    val isOpen: Boolean = false,

    val createdAt: Date?,
    val createdBy: String?,
    val updatedAt: Date?,
    val updatedBy: String?
): Parcelable