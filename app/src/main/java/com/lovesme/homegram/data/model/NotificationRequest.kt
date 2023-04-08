package com.lovesme.homegram.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationRequest(
    val to: String,
    val priority: String = "high",
    val data: NotificationRequestData
)