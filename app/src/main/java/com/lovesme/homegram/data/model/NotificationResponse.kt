package com.lovesme.homegram.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponse(
    val multicast_id: Long,
    val success: Int,
    val failure: Int,
    val canonical_ids: Int,
    val results: List<MessageIdList>
)

@Serializable
data class MessageIdList(val message_id: String)
