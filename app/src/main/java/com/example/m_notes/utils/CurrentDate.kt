package com.example.m_notes.utils

import java.util.*

object CurrentDate {

    fun getCurrentDate(): String {
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        return "$day-$month-$year"
    }
}