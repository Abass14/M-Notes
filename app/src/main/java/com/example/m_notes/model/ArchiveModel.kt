package com.example.m_notes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ArchiveTable")
data class ArchiveModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val note: String,
    val date: String
)
