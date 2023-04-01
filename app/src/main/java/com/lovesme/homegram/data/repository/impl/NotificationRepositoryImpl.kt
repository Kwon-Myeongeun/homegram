package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.NotificationDataSource
import com.lovesme.homegram.data.model.NotificationRequestData
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
) : NotificationRepository {
    override suspend fun sendNotification(
        notification: NotificationRequestData,
        receiverId: String
    ): Result<Unit> {
        return if (notificationDataSource.sendNotification(notification, receiverId)) {
            Result.Success(Unit)
        } else {
            Result.Error(Exception())
        }
    }
}