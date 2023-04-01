package com.lovesme.homegram.data.usecase

import com.lovesme.homegram.data.model.NotificationRequestData
import com.lovesme.homegram.data.model.NotificationType
import com.lovesme.homegram.data.repository.NotificationRepository
import javax.inject.Inject
import com.lovesme.homegram.data.model.Result

class SendNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        notificationType: NotificationType,
        userId: String,
        receiverId: String
    ): Result<Unit> {
        val notification = NotificationRequestData(
            type = notificationType,
            title = notificationType.title,
            message = notificationType.msg,
            fromId = userId,
        )
        return notificationRepository.sendNotification(notification, receiverId)
    }
}