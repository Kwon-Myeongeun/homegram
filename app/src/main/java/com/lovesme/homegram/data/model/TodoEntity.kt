package com.lovesme.homegram.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class TodoEntity(
    @PrimaryKey val key: String,
    val date: String,
    val name: String,
    val contents: String,
) {
}
