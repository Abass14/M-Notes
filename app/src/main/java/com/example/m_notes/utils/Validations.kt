package com.example.m_notes.utils

object Validations {

    fun validateInsertNote(title:String, note: String) : Boolean {
        var result = true
        if(title.isEmpty() || note.isEmpty()){
            result = false
        }
        return result
    }

    fun validateReminderDate(date: String) : Boolean{
        var result = true
        if (date.isEmpty()){
            result = false
        }
        return result
    }

    fun validateReminderTime(time: String) : Boolean{
        var result = true
        if (time.isEmpty()){
            result = false
        }
        return result
    }
}