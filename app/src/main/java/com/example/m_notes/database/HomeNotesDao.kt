package com.example.m_notes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.m_notes.model.HomeNoteModel

@Dao
interface HomeNotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note: HomeNoteModel)

    @Query("SELECT * FROM HomeNoteTable")
    fun getHomeNotes() : LiveData<MutableList<HomeNoteModel>>

    @Query("SELECT * FROM HomeNoteTable WHERE id=:id")
    fun getHomeNotesById(id: Int) : LiveData<HomeNoteModel>

    @Query("DELETE FROM HomeNoteTable WHERE id=:id")
    suspend fun deleteHomeNote(id: Int)

    @Query("UPDATE HomeNoteTable SET title=:title, note=:note, date=:date WHERE id=:id")
    suspend fun updateHomeNote(title: String, note: String, date: String, id:Int)
}