package com.lovesme.homegram.util.network

import com.lovesme.homegram.data.model.NotificationRequest
import com.lovesme.homegram.data.model.NotificationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface CloudMessagingService {
    @POST("fcm/send")
    suspend fun sendNotification(
        @Body notification: NotificationRequest
    ): NotificationResponse
}