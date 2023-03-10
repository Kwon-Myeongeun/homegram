package com.lovesme.homegram.data.model

data class Question(
    val seq: String?,
    val contents: String?,
    val answer: List<Answer>? = null
)
