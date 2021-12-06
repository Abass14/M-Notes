package com.example.m_notes.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object AppSharedPreferences {

    private const val MY_PREF = "My Pref"
    const val SPLASH_KEY = "Slash key"
    const val SET_PASSWORD_KEY = "Set Password key"
    const val NIGHT_MODE_KEY = "Set Night Mode"
//    const val CANCEL_DIALOG_KEY = "Cancel dialog key"
    const val PASSWORD_KEY = "Password"

    lateinit var sharedPreferences: SharedPreferences

    fun initPreference(activity: Activity){
        sharedPreferences = activity.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
    }

    fun setSplashPref(value: Int) {
        sharedPreferences.edit().putInt(SPLASH_KEY, value).apply()
    }
    fun getSlashPref(key: String) : Int{
        return sharedPreferences.getInt(key, 0)
    }

    fun setPasswordPref(value: Int) {
        sharedPreferences.edit().putInt(SET_PASSWORD_KEY, value).apply()
    }
    fun getPasswordPref(key: String) : Int{
        return sharedPreferences.getInt(key, 0)
    }

//    fun setCancelDialogPref(value: Int) {
//        sharedPreferences.edit().putInt(CANCEL_DIALOG_KEY, value).apply()
//    }
//    fun getCancelDialogPref(key: String): Int{
//        return sharedPreferences.getInt(key, 0)
//    }

    fun setPassword(password: String){
        sharedPreferences.edit().putString(PASSWORD_KEY, password).apply()
    }
    fun getPassword(key: String) : String? {
        return sharedPreferences.getString(key, null)
    }

    fun setNightModePref(isNightMode: Boolean){
        sharedPreferences.edit().putBoolean(NIGHT_MODE_KEY, isNightMode).apply()
    }
    fun getNightModePref(key: String): Boolean{
        return sharedPreferences.getBoolean(key, false)
    }
}