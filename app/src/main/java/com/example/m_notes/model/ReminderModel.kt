package com.example.m_notes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ReminderTable")
data class ReminderModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val date: String,
    val time: String,
    val note: String,
    val isSet: Boolean
)