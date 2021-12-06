package com.example.m_notes.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.m_notes.R
import com.example.m_notes.databinding.FragmentProfileBinding
import com.example.m_notes.utils.AppSharedPreferences
import com.example.m_notes.utils.Validations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Profile : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var isChecked: Boolean? = null
    private var setPasswordDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppSharedPreferences.initPreference(requireActivity())
        isChecked =AppSharedPreferences.getNightModePref(AppSharedPreferences.NIGHT_MODE_KEY)
        if (isChecked != null) {
            binding.nightModeSwitch.isChecked = isChecked!!
        }
        clickListeners()
        onBackPressed()
    }

    private fun clickListeners(){
        binding.profileClear.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_home2)
        }

        binding.nightModeSwitch.setOnClickListener {
            if (binding.nightModeSwitch.isChecked){
                AppSharedPreferences.setNightModePref(true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppSharedPreferences.setNightModePref(false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.accountSetPasswordBtn.setOnClickListener {
            showSetPassword()
        }
    }

    private fun showSetPassword() {
        setPasswordDialog = Dialog(requireContext())
        setPasswordDialog?.setContentView(R.layout.password_dialog_layout)
        val password = setPasswordDialog?.findViewById<EditText>(R.id.passwordInput)
        val confirmPassword = setPasswordDialog?.findViewById<EditText>(R.id.confirmPasswordInput)
        val errorTxt = setPasswordDialog?.findViewById<TextView>(R.id.setPasswordErrorTxt)
        val setPasswordBtn = setPasswordDialog?.findViewById<AppCompatButton>(R.id.setPasswordBtn)
        val cancel = setPasswordDialog?.findViewById<AppCompatButton>(R.id.cancelSetBtn)

        password?.addTextChangedListener { editable ->
            if (editable?.length in 1..5){
                errorTxt?.text = "Password has to be up to 6 or more characters"
            }else{
                errorTxt?.setTextColor(resources.getColor(R.color.green_medium))
                errorTxt?.text = "Strong Password"
            }
        }
        setPasswordBtn?.setOnClickListener {
            if (!Validations.validatePasswordStrength(password?.text.toString())){
                errorTxt?.setTextColor(resources.getColor(R.color.red_medium))
                errorTxt?.text = "Password has to be up to 6 or more characters"
            }else if (!Validations.validateSetPasswordEquality(password?.text.toString(), confirmPassword?.text.toString())){
                errorTxt?.setTextColor(resources.getColor(R.color.red_medium))
                errorTxt?.text = "Confirm password not same with Password!!!"
            }else{
                AppSharedPreferences.setPasswordPref(2)
                AppSharedPreferences.setPassword(password?.text.toString())
                setPasswordDialog?.dismiss()
                com.example.m_notes.utils.Dialog.toastMsg(requireContext(), "Password set successfully")
            }
        }

        cancel?.setOnClickListener {
            setPasswordDialog?.dismiss()
        }
        setPasswordDialog?.show()
    }

    override fun onResume() {
        super.onResume()
        onBackPressed()
    }

    private fun onBackPressed(){
        //Overriding onBack press to finish activity and exit app
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_profile_to_home2)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}