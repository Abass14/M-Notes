package com.example.m_notes.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object AppSharedPreferences {

    private const val MY_PREF = "My Pref"
    const val SPLASH_KEY = "Slash key"
    const val SET_PASSWORD_KEY = "Set Password key"
    const val INPUT_PASSWORD_KEY = "Input password key"

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

    fun setInputPasswordPref(value: Int) {
        sharedPreferences.edit().putInt(INPUT_PASSWORD_KEY, value).apply()
    }
    fun getInputPasswordPref(key: String) : Int{
        return sharedPreferences.getInt(key, 0)
    }

}