package com.lovesme.homegram.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val seq: String = "",
    val contents: String = "",
    var answer: List<Answer> = emptyList()
) : Parcelable {
    fun mapToQuestionEntity(): QuestionEntity =
        QuestionEntity(
            seq,
            contents
        )

    fun mapToAnswerEntityList(): List<AnswerEntity> =
        answer.map {
            AnswerEntity(
                seq,
                it.name,
                it.contents
            )
        }
}

