package com.example.m_notes.repository

import androidx.lifecycle.LiveData
import com.example.m_notes.database.ArchiveDao
import com.example.m_notes.database.HomeNotesDao
import com.example.m_notes.database.ReminderDao
import com.example.m_notes.model.ArchiveModel
import com.example.m_notes.model.HomeNoteModel
import com.example.m_notes.model.ReminderModel
import java.lang.Exception
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val homeNotesDao: HomeNotesDao,
    private val archiveDao: ArchiveDao,
    private val reminderDao: ReminderDao
) : MnotesRepository {
    override suspend fun insertHomeNotes(notes: HomeNoteModel) {
        try {
            homeNotesDao.insertNotes(notes)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override val getHomeNotes: LiveData<List<HomeNoteModel>> = homeNotesDao.getHomeNotes()


    override suspend fun deleteHomeNotes(id: Int) {
        try {
            homeNotesDao.deleteHomeNote(id)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun updateHomeNotes(title: String, note: String, date: String, id: Int) {
        try {
            homeNotesDao.updateHomeNote(title, note, date, id)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun getHomeNotesById(id: Int): LiveData<HomeNoteModel> {
        return homeNotesDao.getHomeNotesById(id)
    }

    override suspend fun insertArchivedNotes(notes: ArchiveModel) {
        try {
            archiveDao.insertArchivedNotes(notes)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override val getAllArchivedNotes: LiveData<List<ArchiveModel>> =  archiveDao.getArchivedNotes()

    override suspend fun deleteArchivedNote(id: Int) {
        try {
            archiveDao.deleteArchivedNote(id)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun updateArchivedNote(title: String, note: String, date: String, id: Int) {
        try {
            archiveDao.updateArchivedNote(title, note, date, id)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun getArchivedNoteById(id: Int): LiveData<ArchiveModel> {
        return archiveDao.getArchivedNotesById(id)
    }

    override suspend fun insertReminder(reminder: ReminderModel) {
        try {
            reminderDao.insertReminder(reminder)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override val getReminders : LiveData<List<ReminderModel>> = reminderDao.getReminders()

    override suspend fun deleteReminder(id: Int) {
        try {
            reminderDao.deleteReminder(id)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun updateReminder(year: Int, month: Int,
                                        day: Int, hour: Int, minute: Int,
                                        date: String, time: String, note: String, id:Int, isSet: Boolean, showDialog: Int) {
        try {
            reminderDao.updateReminder(year, month, day, hour, minute, date, time, note, id, isSet, showDialog)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun updateIsSetReminder(isSet: Boolean, id: Int) {
        try {
            reminderDao.updateIsSetReminder(isSet, id)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun updateShowDialog(showDialog: Int, id: Int) {
        try {
            reminderDao.updateShowDialog(showDialog, id)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun getReminderById(id: Int): LiveData<ReminderModel> {
        return reminderDao.getReminderById(id)
    }
}