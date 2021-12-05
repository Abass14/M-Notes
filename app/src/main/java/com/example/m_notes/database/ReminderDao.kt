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
    fun getReminders() : LiveData<List<ReminderModel>>

    @Query("SELECT * FROM ReminderTable WHERE id=:id")
    fun getReminderById(id: Int) : LiveData<ReminderModel>

    @Query("DELETE FROM ReminderTable WHERE id=:id")
    suspend fun deleteReminder(id: Int)

    @Query("UPDATE ReminderTable SET year=:year, month=:month, day=:day, hour=:hour, minute=:minute, date=:date, time=:time, note=:note, isSet=:isSet, showDialog=:showDialog, reminderType=:reminderType WHERE id=:id")
    suspend fun updateReminder(year: Int, month: Int, day: Int, hour: Int, minute: Int, date: String, time: String, note: String, id:Int, isSet: Boolean, showDialog: Int, reminderType: String)

    @Query("UPDATE ReminderTable SET showDialog=:showDialog WHERE id=:id")
    suspend fun updateShowDialog(showDialog: Int, id: Int)

    @Query("UPDATE ReminderTable SET isSet=:isSet WHERE id=:id")
    suspend fun updateIsSetReminder(isSet: Boolean, id: Int)
}