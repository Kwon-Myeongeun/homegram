package com.lovesme.homegram.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationRequestData(
    val title: String,
    val message: String,
    val detail: String
)

enum class NotificationType(val title: String, val msg: String) {
    UPDATE_TODO(
        "일정 알림",
        "일정이 추가되었습니다."
    ),
    UPDATE_ANSWER(
        "답변 알림",
        "답변이 추가되었습니다."
    ),
    UPDATE_QUESTION(
        "질문 알림",
        "오늘의 질문이 추가되었습니다."
    ),
}
