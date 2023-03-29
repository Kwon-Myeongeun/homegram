package com.lovesme.homegram.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question")
data class QuestionEntity(
    @PrimaryKey val key: String = "",
    val seq: String,
    val contents: String,
    val isDone: Boolean = false,
) {
}
