package com.lovesme.homegram.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Answer(
    val seq: String = "",
    val name: String = "",
    val contents: String = ""
) : Parcelable
