package com.lovesme.homegram.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationRequestData(
    val type: String,
    val title: String,
    val message: String,
    val fromId: String
)
