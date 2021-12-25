package com.example.m_notes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.m_notes.R
import com.example.m_notes.model.OnBoardingModel

class OnboardingPagerAdapter(val context: Context): RecyclerView.Adapter<OnboardingPagerAdapter.OnboardingViewHolder>() {
    var data: MutableList<OnBoardingModel> = mutableListOf()
    class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var sliderImage: ImageView = view.findViewById(R.id.onBoardingLogo)
        var sliderTxt: TextView = view.findViewById(R.id.onBoardingTxt)

        fun bind(item: OnBoardingModel){
            sliderImage.setImageResource(item.image)
            sliderTxt.text = item.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.onboarding_layout, parent, false)
        return OnboardingViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}