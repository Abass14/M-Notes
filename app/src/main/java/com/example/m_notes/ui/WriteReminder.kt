package com.example.m_notes.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.m_notes.databinding.FragmentWriteReminderBinding
import java.text.DateFormat
import java.util.*
import android.widget.TimePicker

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteReminder : Fragment() {
    private var _binding: FragmentWriteReminderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWriteReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }

    private fun clickListeners() {
        val mCalender = Calendar.getInstance()
        binding.reminderClear.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.setReminderDateBtn.setOnClickListener {
            val year = mCalender.get(Calendar.YEAR)
            val month = mCalender.get(Calendar.MONTH)
            val day = mCalender.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireContext(),
                OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    binding.reminderDateTxt.text =  dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                }, year, month, day
            )
            datePickerDialog.show()
        }

        binding.setReminderTimeBtn.setOnClickListener {
            val hour = mCalender.get(Calendar.HOUR_OF_DAY)
            val minute = mCalender.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(requireContext(),
                OnTimeSetListener { _, hourOfDay, minute ->
                    binding.reminderTimeTxt.text = "$hourOfDay:$minute" },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}