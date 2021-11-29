package com.example.m_notes.viewmodel

import android.util.Log
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

    private var _allNotesLiveData: MutableLiveData<MutableList<HomeNoteModel>> = MutableLiveData()
    val allNotesLiveData: LiveData<List<HomeNoteModel>> = mnotesRepository.getHomeNotes

    private val _noteByIdLiveData: MutableLiveData<HomeNoteModel> = MutableLiveData()
    var noteByIdLiveData: LiveData<HomeNoteModel>? = null

    private val _allArchivedNoteLiveData: MutableLiveData<List<ArchiveModel>> = MutableLiveData()
    val allArchivedNoteLiveData: LiveData<List<ArchiveModel>> = mnotesRepository.getAllArchivedNotes

    private val _archivedNoteByIdLiveData: MutableLiveData<ArchiveModel> = MutableLiveData()
    var archivedNoteByIdLiveData: LiveData<ArchiveModel>? = null

    private val _allReminderLiveData: MutableLiveData<MutableList<ReminderModel>> = MutableLiveData()
    val allReminderLiveData: LiveData<MutableList<ReminderModel>> = _allReminderLiveData

    private val _reminderById: MutableLiveData<ReminderModel> = MutableLiveData()
    val reminderByIdLiveData: LiveData<ReminderModel> = _reminderById

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
                noteByIdLiveData = response
                Log.d("AppViewModel: Item", "Success")
            }catch (e: Exception){
                e.printStackTrace()
                Log.d("AppViewModel: Item", "Failed")
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
                Log.d("AppViewModel: Update", "sUCCESS")
            }catch (e: Exception){
                e.printStackTrace()
                Log.d("AppViewModel: Update", "Failed")
            }
        }
    }

    fun insertArchivedNotes (title: String, note: String, date: String) {
        val archivedNote = ArchiveModel(0, title, note, date)
        viewModelScope.launch(Dispatchers.IO){
            try {
                mnotesRepository.insertArchivedNotes(archivedNote)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateArchivedNote (title: String, note: String, date: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                mnotesRepository.updateArchivedNote(title, note, date, id)
                Log.d("AppViewModel", "Success")
            }catch (e: Exception){
                e.printStackTrace()
                Log.d("AppViewModel", "Failed")
            }
        }
    }

    fun deleteArchivedNote (id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                mnotesRepository.deleteArchivedNote(id)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun getArchivedNoteById(id: Int) {
        viewModelScope.launch {
            try {
                val response = mnotesRepository.getArchivedNoteById(id)
                archivedNoteByIdLiveData = response
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}