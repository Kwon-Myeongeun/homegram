package com.lovesme.homegram.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groupInfo")
data class GroupEntity(
    @PrimaryKey val name: String,
) {
}
