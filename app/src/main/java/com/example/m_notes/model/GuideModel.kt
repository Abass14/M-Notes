package com.example.m_notes.model

import com.example.m_notes.R

data class GuideModel(
    val text: String,
    val color: Int
) {
    companion object {
        val guideList = listOf(
            GuideModel("M-Notes Guide", R.color.blue_medium),
            GuideModel("Click on plus button to add notes", R.color.green_medium),
            GuideModel("Click Notes to view detailed notes", R.color.black),
            GuideModel("Long press notes to archive or delete", R.color.red_dark)
        )
    }
}
