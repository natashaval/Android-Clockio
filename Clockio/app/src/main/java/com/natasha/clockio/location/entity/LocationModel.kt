package com.natasha.clockio.location.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationModel (
    var latitude: Double,
    var longitude: Double
) : Parcelable