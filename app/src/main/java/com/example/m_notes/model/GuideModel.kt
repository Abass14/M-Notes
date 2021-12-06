package com.example.m_notes.model

import com.example.m_notes.R

data class GuideModel(
    val text: String,
    val color: Int
) {
    companion object {
        val guideList = listOf(
            GuideModel("M-Notes Guide", R.color.orange),
            GuideModel("Go to settings to switch to night mode", R.color.orange),
            GuideModel("Long press notes/reminders to archive or delete", R.color.red_dark),
            GuideModel("Go to settings to reset Archive password", R.color.orange),
            GuideModel("Click on plus button to add notes/reminders", R.color.green_medium),
            GuideModel("Click Notes to view detailed notes", R.color.black),
        )
    }
}
