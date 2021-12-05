package com.example.m_notes.utils

import android.os.Build
import androidx.annotation.RequiresApi

const val REQUEST_CODE = 1
@RequiresApi(Build.VERSION_CODES.S)
val PERMISSION_ARRAY = arrayOf(android.Manifest.permission.SCHEDULE_EXACT_ALARM)

const val NOTIFICATION_ID = 0
const val PRIMARY_CHANNEL_ID = "primary_notification_channel"