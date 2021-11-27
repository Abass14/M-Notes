package com.example.m_notes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.m_notes.model.ArchiveModel
import com.example.m_notes.model.HomeNoteModel
import com.example.m_notes.model.ReminderModel

@Database(
    entities = [
        HomeNoteModel::class,
        ArchiveModel::class,
        ReminderModel::class
               ],
    version = 1,
    exportSchema = false
)
abstract class M_notesDatabase : RoomDatabase() {
    abstract fun getHomeNotesDao() : HomeNotesDao
    abstract fun getArchiveDao() : ArchiveDao
    abstract fun getReminderDao() : ReminderDao

    companion object {
        @Volatile
        private var notesDbInstance: M_notesDatabase? = null
        private var lock = Any()

        operator fun invoke(context: Context) = notesDbInstance?: synchronized(lock){
            notesDbInstance?: getDbInstance(context)
        }

        fun getDbInstance(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            M_notesDatabase::class.java,
            "M_NotesDb"
        ).build()
    }

}