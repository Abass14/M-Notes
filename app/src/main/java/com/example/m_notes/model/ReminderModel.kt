package com.example.m_notes.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "ReminderTable")
@Parcelize
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
    val isSet: Boolean,
    val showDialog: Int,
    val reminderType: String,
    val reminderPosition: Int
) : Parcelable