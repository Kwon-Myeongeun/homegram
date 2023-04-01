package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.NotificationRequestData

interface NotificationDataSource {
    suspend fun sendNotification(notification: NotificationRequestData, to: String): Boolean
}