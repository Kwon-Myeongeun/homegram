package com.lovesme.homegram.data.usecase

import com.lovesme.homegram.data.model.NotificationRequestData
import com.lovesme.homegram.data.repository.NotificationRepository
import javax.inject.Inject
import com.lovesme.homegram.data.model.Result

class SendNotificationUseCase @Inject constructor(private val notificationRepository: NotificationRepository) {
    suspend operator fun invoke(
        userId: String,
        receiverId: String
    ): Result<Unit> {
        val notification = NotificationRequestData(
            type = "Todo",
            title = "Todo update",
            message = "Todo가 업데이트됨",
            fromId = userId,
        )
        return notificationRepository.sendNotification(notification, receiverId)
    }
}