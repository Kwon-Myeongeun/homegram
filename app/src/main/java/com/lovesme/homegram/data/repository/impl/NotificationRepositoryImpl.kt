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
        val receiverToken = "egCh-2ilQTafokC3sTwxFJ:APA91bETKQjCNany7nw2C-d7l5OjrekYsghOeco2QhqtrdaSDwNlQRFb0ex_C5vz2xzi_GoSrVJh4SUB4lc3R4QvWGWTcll_oAbLiWyu3QihsWhO8IFrCH4u1b_BjI2vu5dM8Lu3OPUS"
        return if (notificationDataSource.sendNotification(notification, receiverToken)) {
            Result.Success(Unit)
        } else {
            Result.Error(Exception())
        }
    }
}