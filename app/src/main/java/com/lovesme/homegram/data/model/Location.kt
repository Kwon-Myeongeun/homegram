package com.lovesme.homegram.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val latitude: Double = 128.624,
    val longitude: Double = 36.805,
    var title: String = "",
) : Parcelable {
    fun mapToLocationEntity(): LocationEntity =
        LocationEntity(
            latitude,
            longitude,
            title
        )
}