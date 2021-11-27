package com.example.m_notes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ReminderTable")
data class ReminderModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    val time: String,
    val note: String
)