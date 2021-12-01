package com.example.m_notes.utils

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.m_notes.MainActivity

class AlarmReceiver(val title: String? = null) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val description = intent.getStringExtra("Description")
        Log.d("AlarmR", description!!)
       val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK / Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        Reminder.deliverReminder(context, pendingIntent, description)
    }
}