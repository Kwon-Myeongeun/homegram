package com.lovesme.homegram.data.repository

import com.lovesme.homegram.data.model.NotificationRequestData
import com.lovesme.homegram.data.model.Result

interface NotificationRepository {
    suspend fun sendNotification(
        notification: NotificationRequestData,
        receiverId: String
    ): Result<Unit>
}