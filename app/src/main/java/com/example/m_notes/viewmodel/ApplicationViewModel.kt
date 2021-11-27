package com.example.m_notes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.m_notes.model.ArchiveModel
import com.example.m_notes.model.HomeNoteModel
import com.example.m_notes.model.ReminderModel
import com.example.m_notes.repository.MnotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(private val mnotesRepository: MnotesRepository) : ViewModel() {

    private val _allNotesLiveData: MutableLiveData<MutableList<HomeNoteModel>> = MutableLiveData()
    val allNotesLiveData: LiveData<MutableList<HomeNoteModel>> = _allNotesLiveData

    private val _noteByIdLiveData: MutableLiveData<HomeNoteModel> = MutableLiveData()
    val noteByIdLiveData: LiveData<HomeNoteModel> = _noteByIdLiveData

    private val _allArchivedNoteLiveData: MutableLiveData<MutableList<ArchiveModel>> = MutableLiveData()
    val allArchivedNoteLiveData: LiveData<MutableList<ArchiveModel>> = _allArchivedNoteLiveData

    private val _archivedNoteByIdLiveData: MutableLiveData<ArchiveModel> = MutableLiveData()
    val archivedNoteByIdLiveData: LiveData<ArchiveModel> = _archivedNoteByIdLiveData

    private val _allReminderLiveData: MutableLiveData<MutableList<ReminderModel>> = MutableLiveData()
    val allReminderLiveData: LiveData<MutableList<ReminderModel>> = _allReminderLiveData

    private val _reminderById: MutableLiveData<ReminderModel> = MutableLiveData()
    val reminderByIdLiveData: LiveData<ReminderModel> = _reminderById

    fun getAllHomeNotes () {
        viewModelScope.launch {
            try {
               val response = mnotesRepository.getHomeNotes()
                _allNotesLiveData.value = response.value
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun insertHomeNotes (title: String, note: String, date: String) {
        val homeNote = HomeNoteModel(0, title, note, date)
        viewModelScope.launch(Dispatchers.IO){
            try {
                mnotesRepository.insertHomeNotes(homeNote)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getNoteById (id: Int) {
        viewModelScope.launch {
            try {
                val response = mnotesRepository.getHomeNotesById(id)
                _noteByIdLiveData.value = response.value
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun deleteNote (id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                mnotesRepository.deleteHomeNotes(id)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateNote (title: String, note: String, date: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                mnotesRepository.updateHomeNotes(title, note, date, id)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}