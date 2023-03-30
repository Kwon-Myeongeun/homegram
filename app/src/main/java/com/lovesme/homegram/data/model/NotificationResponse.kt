package com.lovesme.homegram.data.model

import kotlinx.serialization.Serializable

data class NotificationResponse(
    val success: Int,
    val failure: Int,
    val results: List<MessageIdList>
)

@Serializable
data class MessageIdList(val messageId: String)
