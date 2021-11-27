package com.example.m_notes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.m_notes.model.ArchiveModel
import com.example.m_notes.model.HomeNoteModel

@Dao
interface ArchiveDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArchivedNotes(note: ArchiveModel)

    @Query("SELECT * FROM ArchiveTable")
    fun getArchivedNotes() : LiveData<MutableList<ArchiveModel>>

    @Query("SELECT * FROM ArchiveTable WHERE id=:id")
    fun getArchivedNotesById(id: Int) : LiveData<ArchiveModel>

    @Query("DELETE FROM ArchiveTable WHERE id=:id")
    suspend fun deleteArchivedNote(id: Int)

    @Query("UPDATE ArchiveTable SET title=:title, note=:note, date=:date WHERE id=:id")
    suspend fun updateArchivedNote(title: String, note: String, date: String, id:Int)
}