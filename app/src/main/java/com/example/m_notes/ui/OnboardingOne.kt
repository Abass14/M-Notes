package com.example.m_notes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.m_notes.R
import com.example.m_notes.adapter.OnboardingPagerAdapter
import com.example.m_notes.databinding.FragmentOnboardingOneBinding
import com.example.m_notes.model.OnBoardingModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingOne : Fragment() {
    private var _binding: FragmentOnboardingOneBinding? = null
    private val binding get() = _binding!!

    lateinit var adapterV: OnboardingPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOnboardingOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterV = OnboardingPagerAdapter(requireContext())
        binding.viewPager.adapter = adapterV
        adapterV.data = OnBoardingModel.dataList

        binding.nextBtn.setOnClickListener {
            if (getItem(0) < 2){
                binding.viewPager.setCurrentItem(getItem(1), true)
            }else{
                findNavController().navigate(R.id.action_onboardingOne_to_home2)
            }
        }

        binding.skipTxt.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingOne_to_home2)
        }
        setUpIndicator()
        setUpViewPagerPageListener()
    }

    private fun setUpIndicator() {
        binding.circularIndicator.setViewPager(binding.viewPager)
    }

    private fun setUpViewPagerPageListener(){
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                textChangeOnClickEvent(position)
            }
        })
    }

    private fun getItem (position: Int): Int{
        return binding.viewPager.currentItem + position
    }

    fun textChangeOnClickEvent(position: Int) {
        if (position < adapterV.itemCount - 1) {
            binding.skipTxt.text = getString(R.string.skip)
            binding.nextBtn.text = getString(R.string.next)
        } else {
            binding.skipTxt.text = getString(R.string.done)
            binding.nextBtn.text = getString(R.string.get_started)
        }
    }

    fun onBackPressed(){
        //Overriding onBack press to finish activity and exit app
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}