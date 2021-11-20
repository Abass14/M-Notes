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
//    override fun getCount(): Int {
//        return data.size
//    }
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view == `object` as ConstraintLayout
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val view = LayoutInflater.from(container.context).inflate(R.layout.onboarding_layout, container, false)
//
//        data.add(OnBoardingModel(R.drawable.ic_baseline_sticky_note_2_24, "Add New Notes"))
//        data.add(OnBoardingModel(R.drawable.ic_archive_24, "Archive Notes"))
//        data.add(OnBoardingModel(R.drawable.ic_baseline_calendar_today_24, "Set Reminders"))
//
//        val sliderImage: ImageView = view.findViewById(R.id.onBoardingLogo)
//        val sliderTxt: TextView = view.findViewById(R.id.onBoardingTxt)
//        sliderImage.setImageResource(data[position].image)
//        sliderTxt.text = data[position].text
//
//        container.addView(view)
//
//        return view
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        container.removeView(`object` as ConstraintLayout)
//    }
}