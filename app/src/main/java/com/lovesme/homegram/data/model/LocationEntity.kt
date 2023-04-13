package com.lovesme.homegram.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    val latitude: Double,
    val longitude: Double,
    @PrimaryKey var title: String = "",
) {
}