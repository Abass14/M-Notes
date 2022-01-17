package com.example.m_notes.utils

import java.util.*

object CurrentDate {
    val calender = Calendar.getInstance()
    val year = calender.get(Calendar.YEAR)
    val month = calender.get(Calendar.MONTH)
    val day = calender.get(Calendar.DAY_OF_MONTH)
    val hour = calender.get(Calendar.HOUR_OF_DAY)
    val minute = calender.get(Calendar.MINUTE)

    fun getCurrentDate(): String {
        return "$day-${month+1}-$year"
    }
}