package com.example.m_notes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.m_notes.model.HomeNoteModel
import com.example.m_notes.model.ReminderModel

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderModel)

    @Query("SELECT * FROM ReminderTable")
    fun getReminders() : LiveData<MutableList<ReminderModel>>

    @Delete
    suspend fun deleteReminder(reminder: ReminderModel)

    @Query("UPDATE ReminderTable SET date=:date, time=:time, note=:note WHERE id=:id")
    suspend fun updateReminder(date: String, time: String, note: String, id:Int)
}