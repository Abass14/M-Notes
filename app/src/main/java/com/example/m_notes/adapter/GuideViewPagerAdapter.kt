package com.example.m_notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.m_notes.R
import com.example.m_notes.model.GuideModel

class GuideViewPagerAdapter() : RecyclerView.Adapter<GuideViewPagerAdapter.GuideViewHolder>() {
    var guideList: List<GuideModel> = listOf()
    class GuideViewHolder (view: View): RecyclerView.ViewHolder(view){
        val text: TextView = view.findViewById(R.id.guideText)
        val guidCardView: CardView = view.findViewById(R.id.guideCardView)
        fun bind(guideView: GuideModel){
            text.text = (guideView.text)
            guidCardView.setCardBackgroundColor(guideView.color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.guide_layout, parent, false)
        return GuideViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        holder.bind(guideList[position])
    }

    override fun getItemCount(): Int {
        return guideList.size
    }
}