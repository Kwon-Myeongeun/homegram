package com.lovesme.homegram.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answer")
data class AnswerEntity(
    val key: String,
    val name: String,
    val contents: String
) {
    @PrimaryKey(autoGenerate = true)
    var answerId: Int = 0
}
