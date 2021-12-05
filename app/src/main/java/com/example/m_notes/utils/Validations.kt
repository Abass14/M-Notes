package com.example.m_notes.utils

object Validations {

    fun validateInsertNote(title:String, note: String) : Boolean {
        var result = true
        if(title.isEmpty() || note.isEmpty()){
            result = false
        }
        return result
    }



    fun validateReminderDate(year: Int?, month: Int?, day: Int?,
                            hour: Int?, minute: Int?, reminderType: String?) : Boolean{
        var result = true
        if (year == null && month == null && day == null && hour == null && minute == null && (reminderType == null || reminderType.isEmpty())){
            result = false
        }
        return result
    }

    fun validateYear(year: Int?) : Boolean{
        var result = true
        if (year == null){
            result = false
        }
        return result
    }
    fun validateMonth(month: Int?) : Boolean{
        var result = true
        if (month == null){
            result = false
        }
        return result
    }
    fun validateDay(day: Int?) : Boolean{
        var result = true
        if (day == null){
            result = false
        }
        return result
    }
    fun validateHour(hour: Int?) : Boolean{
        var result = true
        if (hour == null){
            result = false
        }
        return result
    }
    fun validateMinute(minute: Int?) : Boolean{
        var result = true
        if (minute == null){
            result = false
        }
        return result
    }
    fun validateReminderType(reminderType: String?) : Boolean{
        var result = true
        if (reminderType != "Daily" || reminderType != "One-time"){
            result = false
        }
        return result
    }
    fun validateDailyReminderDate(year: Int?) : Boolean{
        var result = true
        if (year != null) {
            if (year < CurrentDate.year){
                result = false
            }
        }
        return result
    }

    fun validateOneTimeReminderDate(year: Int?, month: Int?, day: Int?) : Boolean{
        var result = true
    if (year != null && month != null && day != null) {
        if (year < CurrentDate.year || month < CurrentDate.month || day < CurrentDate.day){
            result = false
        }
    }
        return result
    }
    fun validateOneTimeReminderTime(hour: Int?, minute: Int?) : Boolean{
        var result = true
        if (hour != null && minute != null) {
            if (hour < CurrentDate.hour || minute < CurrentDate.minute){
                result = false
            }
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

    fun validateSetPasswordEquality(password: String, confirmPassword: String) : Boolean{
        var result = true
        if (password != confirmPassword){
            result = false
        }
        return result
    }
    fun validatePasswordStrength(password: String) : Boolean{
        var result = true
        if (password.isEmpty() || password.length < 6){
            result = false
        }
        return result
    }
}