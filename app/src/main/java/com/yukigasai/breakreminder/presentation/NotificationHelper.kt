package com.yukigasai.breakreminder.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.yukigasai.breakreminder.R

object NotificationHelper {

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            Constants.Notification.CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            vibrationPattern = longArrayOf(0, 500, 200, 200, 200, 100)
            description = context.getString(R.string.notification_channel_description)
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun sendNotification(context: Context, task: String) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val notificationManager = NotificationManagerCompat.from(context)

        val notification = NotificationCompat.Builder(context, Constants.Notification.CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(task)
            .setSmallIcon(R.drawable.baseline_trending_up_24)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()

        notificationManager.notify(1, notification)
    }
}