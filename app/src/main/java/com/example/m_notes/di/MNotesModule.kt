package com.example.m_notes.di

import android.content.Context
import com.example.m_notes.database.ArchiveDao
import com.example.m_notes.database.HomeNotesDao
import com.example.m_notes.database.M_notesDatabase
import com.example.m_notes.database.ReminderDao
import com.example.m_notes.repository.MnotesRepository
import com.example.m_notes.repository.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MNotesModule {

    @Singleton
    @Provides
    fun provideDatabaseInstance(@ApplicationContext context: Context) : M_notesDatabase = M_notesDatabase.getDbInstance(context)

    @Singleton
    @Provides
    fun provideHomeNotesDao(mNotesDatabase: M_notesDatabase) : HomeNotesDao = mNotesDatabase.getHomeNotesDao()

    @Singleton
    @Provides
    fun provideArchiveNotesDao(mNotesDatabase: M_notesDatabase) : ArchiveDao = mNotesDatabase.getArchiveDao()

    @Singleton
    @Provides
    fun provideReminderNotesDao(mNotesDatabase: M_notesDatabase) : ReminderDao = mNotesDatabase.getReminderDao()

    @Singleton
    @Provides
    fun provideMNotesRepository(homeNotesDao: HomeNotesDao,
                                archiveDao: ArchiveDao,
                                reminderDao: ReminderDao) : MnotesRepository {
        return NotesRepository(homeNotesDao, archiveDao, reminderDao)
    }

}