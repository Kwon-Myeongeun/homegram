package com.lovesme.homegram.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val key: String = "",
    val no: String = "",
    val contents: String = "",
    val isDone: Boolean = false,
    var answer: List<Answer> = emptyList()
) : Parcelable {
    fun mapToQuestionEntity(): QuestionEntity =
        QuestionEntity(
            key,
            no,
            contents,
            isDone
        )

    fun mapToAnswerEntityList(): List<AnswerEntity> =
        answer.map {
            AnswerEntity(
                key,
                it.name,
                it.contents
            )
        }
}

