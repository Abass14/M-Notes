package com.example.m_notes.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.m_notes.R
import android.media.AudioAttributes




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
        val sound = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.reminder_sound)
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        val builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_sticky_note_2_24)
            .setContentTitle("Don't Forget!!!")
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(sound)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)

        notificationManager.notify(NOTIFICATION_ID, builder.build())


    }
}