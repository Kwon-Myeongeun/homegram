package com.lovesme.homegram.data.usecase

import android.content.Context
import com.lovesme.homegram.data.model.NotificationRequestData
import com.lovesme.homegram.data.model.NotificationType
import com.lovesme.homegram.data.repository.NotificationRepository
import javax.inject.Inject
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext

class SendNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(
        notificationType: NotificationType,
        userId: String,
    ): Result<Unit> {
        val notification = NotificationRequestData(
            type = notificationType,
            title = notificationType.title,
            message = notificationType.msg,
            fromId = userId,
        )
        val receiverIds = userPreferencesRepository.getReceiverToken()
        var result: Result<Unit> = Result.Success(Unit)
        return if (receiverIds is Result.Success) {
            for (receiverId in (receiverIds.data as List<String>)) {
                val temp = notificationRepository.sendNotification(notification, receiverId)
                if (temp is Result.Error) {
                    result = temp
                }
            }
            result
        } else {
            Result.Error((receiverIds as Result.Error).exception)
        }
    }
}