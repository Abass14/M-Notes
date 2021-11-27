package com.example.m_notes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.m_notes.model.ArchiveModel

@Dao
interface ArchiveDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArchivedNotes(note: ArchiveModel)

    @Query("SELECT * FROM ArchiveTable")
    fun getArchivedNotes() : LiveData<MutableList<ArchiveModel>>

    @Delete
    suspend fun deleteArchivedNote(notes: ArchiveModel)

    @Query("UPDATE ArchiveTable SET title=:title, note=:note, date=:date WHERE id=:id")
    suspend fun updateArchivedNote(title: String, note: String, date: String, id:Int)
}