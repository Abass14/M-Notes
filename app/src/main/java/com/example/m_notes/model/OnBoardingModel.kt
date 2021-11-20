package com.example.m_notes.model

import com.example.m_notes.R

data class OnBoardingModel(
    val image: Int,
    val text: String
){
    companion object{
        val dataList = mutableListOf<OnBoardingModel>(
            OnBoardingModel(R.drawable.ic_baseline_sticky_note_2_24, "Add New Notes"),
            OnBoardingModel(R.drawable.ic_archive_24, "Archive Notes"),
            OnBoardingModel(R.drawable.ic_baseline_calendar_today_24, "Set Reminders")
        )
    }
}