package com.lovesme.homegram.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lovesme.homegram.R
import com.lovesme.homegram.presentation.ui.SplashActivity
import com.lovesme.homegram.presentation.ui.main.MainActivity

class NotificationService : FirebaseMessagingService() {

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // ToDo 서버에 토큰 저장
        Log.d("FCM", "onNewToken start")
        Log.d("FCM", "token: $token")
        Log.d("FCM", "onNewToken end")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val channel = NotificationChannel(
            getString(R.string.default_notification_channel_id),
            getString(R.string.default_notification_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, SplashActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder =
            NotificationCompat.Builder(
                this,
                getString(R.string.default_notification_channel_id)
            )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message.data["title"])
                .setContentText(message.data["message"])
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(0, builder.build())
    }

    private fun createPendingIntent(message: RemoteMessage): PendingIntent? {
        // 딥링크 생성
        val deepLinkBuilder = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            //.setGraph(R.navigation.home_graph)

        return deepLinkBuilder.createPendingIntent()
    }
}