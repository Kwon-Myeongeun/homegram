package com.lovesme.homegram.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val seq: String,
    val contents: String,
    val answer: List<Answer>? = null
) : Parcelable

