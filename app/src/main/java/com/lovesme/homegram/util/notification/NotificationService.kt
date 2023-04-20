package com.lovesme.homegram.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lovesme.homegram.R
import com.lovesme.homegram.data.model.NotificationType
import com.lovesme.homegram.data.usecase.SetMessageTokenUseCase
import com.lovesme.homegram.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Inject
    lateinit var setMessageTokenUseCase: SetMessageTokenUseCase

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            setMessageTokenUseCase(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.default_notification_channel_id),
                getString(R.string.default_notification_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder =
            NotificationCompat.Builder(
                this,
                getString(R.string.default_notification_channel_id)
            )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message.data["title"])
                .setContentText(message.data["message"])
                .setContentIntent(createPendingIntent(message))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(0, builder.build())
    }

    private fun createPendingIntent(message: RemoteMessage): PendingIntent {
        val type = message.data["title"]

        val deepLinkBuilder = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.home_graph)

        when (type) {
            NotificationType.UPDATE_TODO.title -> {
                deepLinkBuilder.apply {
                    addDestination(R.id.navigation_calendar_menu)
                }
            }
            NotificationType.UPDATE_ANSWER.title -> {
                deepLinkBuilder.apply {
                    addDestination(R.id.navigation_daily_menu)
                }
            }
        }

        return deepLinkBuilder.createPendingIntent()
    }
}