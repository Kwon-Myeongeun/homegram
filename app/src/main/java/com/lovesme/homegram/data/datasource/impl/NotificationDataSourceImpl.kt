package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.datasource.NotificationDataSource
import com.lovesme.homegram.data.model.NotificationRequest
import com.lovesme.homegram.data.model.NotificationRequestData
import com.lovesme.homegram.util.network.CloudMessagingService
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(private val cloudMessagingService: CloudMessagingService) :
    NotificationDataSource {
    override suspend fun sendNotification(
        notification: NotificationRequestData,
        to: String
    ): Boolean {
        return runCatching {
            cloudMessagingService.sendNotification(
                NotificationRequest(
                    to = to,
                    data = notification
                )
            )
        }.isSuccess
    }
}