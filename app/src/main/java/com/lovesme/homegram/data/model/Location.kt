package com.lovesme.homegram.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    var title: String = "",
    var userId: String = "",
) : Parcelable