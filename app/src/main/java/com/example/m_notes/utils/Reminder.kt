package com.example.m_notes.utils

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.m_notes.R

object Reminder {

    fun createNotificationChannel(notificationManager: NotificationManager, primaryChannelId: String){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(primaryChannelId, "M-Note", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = R.color.red_medium
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Event reminder"
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun deliverReminder(context: Context, pendingIntent: PendingIntent, description: String){
        val notificationManager = NotificationManagerCompat.from(context)
        val builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("title")
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)

        notificationManager.notify(NOTIFICATION_ID, builder.build())

    }
}