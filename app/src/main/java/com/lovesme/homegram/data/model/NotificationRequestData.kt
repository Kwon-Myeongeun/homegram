package com.lovesme.homegram.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationRequestData(
    val type: NotificationType,
    val title: String,
    val message: String,
    val fromId: String
)

enum class NotificationType(val title: String, val msg: String) {
    UPDATE_TODO(
        "Homegram Todo",
        "일정이 추가되었습니다."
    ),
    UPDATE_ANSWER(
        "Homegram answer",
        "답변이 추가되었습니다."
    ),
    UPDATE_QUESTION(
        "Homegram question",
        "오늘의 질문이 추가되었습니다."
    ),
}
