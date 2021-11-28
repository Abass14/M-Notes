package com.example.m_notes.repository

import androidx.lifecycle.LiveData
import com.example.m_notes.model.ArchiveModel
import com.example.m_notes.model.HomeNoteModel
import com.example.m_notes.model.ReminderModel

interface MnotesRepository {
    suspend fun insertHomeNotes (notes: HomeNoteModel)
    val getHomeNotes: LiveData<List<HomeNoteModel>>
    suspend fun deleteHomeNotes (id: Int)
    suspend fun updateHomeNotes (title: String, note: String, date: String, id:Int)
    fun getHomeNotesById (id: Int) : LiveData<HomeNoteModel>

    suspend fun insertArchivedNotes (notes: ArchiveModel)
    fun getArchivedNotes () : LiveData<MutableList<ArchiveModel>>
    suspend fun deleteArchivedNote (id: Int)
    suspend fun updateArchivedNote (title: String, note: String, date: String, id:Int)
    fun getArchivedNoteById (id: Int) : LiveData<ArchiveModel>

    suspend fun insertReminder (reminder: ReminderModel)
    fun getReminders () : LiveData<MutableList<ReminderModel>>
    suspend fun deleteReminder (id: Int)
    suspend fun updateReminder (date: String, time: String, note: String, id:Int)
    fun getReminderById (id: Int) : LiveData<ReminderModel>
}