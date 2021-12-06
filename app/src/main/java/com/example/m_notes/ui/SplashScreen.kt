package com.example.m_notes.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.m_notes.R
import com.example.m_notes.databinding.FragmentSplashScreenBinding
import com.example.m_notes.utils.AppSharedPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreen : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private var splashPrefValue: Int? = null
    private var reminderFragment: String? = null
    val handler = Handler(Looper.getMainLooper())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        AppSharedPreferences.initPreference(requireActivity())
        nightModeCheck()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun navigate(){
        reminderFragment = requireActivity().intent.getStringExtra("ReminderFragment")
        splashPrefValue = AppSharedPreferences.getSlashPref(AppSharedPreferences.SPLASH_KEY)
        if (reminderFragment != null && reminderFragment == "Reminder"){
            handler.postDelayed({
                findNavController().navigate(R.id.action_splashScreen_to_reminder)
            }, 2000)
        }else {
            if (splashPrefValue != null) {
                handler.postDelayed({
                    if (splashPrefValue!! == 1) {
                        lifecycleScope.launchWhenResumed {
                            findNavController().navigate(R.id.action_splashScreen_to_home2)
                        }
                    } else {
                        findNavController().navigate(R.id.action_splashScreen_to_onboardingOne)
                    }
                }, 2000)
            }
        }
    }

    private fun nightModeCheck(){
        val nightModeStatus = AppSharedPreferences.getNightModePref(AppSharedPreferences.NIGHT_MODE_KEY)

        if (nightModeStatus){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onResume() {
        super.onResume()
        navigate()
//        nightModeCheck()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}