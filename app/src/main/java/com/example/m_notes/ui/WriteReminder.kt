package com.example.m_notes.ui

import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.m_notes.databinding.FragmentWriteReminderBinding

import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.GregorianCalendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.m_notes.R
import com.example.m_notes.utils.*
import com.example.m_notes.utils.Dialog
import com.example.m_notes.utils.Reminder
import com.example.m_notes.viewmodel.ApplicationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteReminder : Fragment() {
    private var _binding: FragmentWriteReminderBinding? = null
    private val binding get() = _binding!!
    private var reminderHour: Int? = null
    private var reminderMinute: Int? = null
    private var reminderDay: Int? = null
    private var reminderMonth: Int? = null
    private var reminderYear: Int? = null
    private val viewModel: ApplicationViewModel by viewModels()
    private val successDialog: MaterialAlertDialogBuilder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWriteReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun clickListeners() {
        binding.reminderClear.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.setReminderDateBtn.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(),
                OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    "$dayOfMonth-${monthOfYear + 1}-$year".also { binding.reminderDateTxt.text = it }
                    reminderMonth = monthOfYear
                    reminderYear = year
                    reminderDay = dayOfMonth
                }, CurrentDate.year, CurrentDate.month, CurrentDate.day
            )
            datePickerDialog.show()
        }

        binding.setReminderTimeBtn.setOnClickListener {

            val timePickerDialog = TimePickerDialog(requireContext(),
                OnTimeSetListener { _, hourOfDay, minute ->
                    binding.reminderTimeTxt.text = "$hourOfDay:$minute"
                    reminderHour = hourOfDay
                    reminderMinute = minute },
                CurrentDate.hour,
                CurrentDate.minute,
                true
            )
            timePickerDialog.show()
        }

        binding.reminderSave.setOnClickListener {
            if (reminderYear != null && reminderMonth != null && reminderDay != null && reminderHour != null && reminderMinute != null && binding.reminderEditText.text.toString().isNotEmpty()) {
                if (reminderYear!! >= CurrentDate.year && reminderDay!! >= CurrentDate.day
                    && reminderMonth!! >= CurrentDate.month
                ) {
                    insertReminder(
                        reminderYear!!, reminderMonth!!,
                        reminderDay!!, reminderHour!!, reminderMinute!!,
                        binding.reminderDateTxt.text.toString(),
                        binding.reminderTimeTxt.text.toString(),
                        binding.reminderEditText.text.toString()
                    )
                    showSuccessDialog()
                    findNavController().popBackStack()
                }else{
                    Dialog.toastMsg(requireContext(), "Invalid Date Selection!!!")
                }
            }else{
                Dialog.toastMsg(requireContext(), "Date, Time or Reminder Text Field can't be empty can't be left empty")
            }
        }

    }

    private fun showSuccessDialog(){
        val positiveTask = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        val negativeTask = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        Dialog.alertDialog(successDialog, requireActivity(), requireContext(),
        "Reminder Successfully Logged!!", "Toggle the Switch button to Schedule or Cancel Reminder",
        "OK", "", null, null, R.style.RoundShapeTheme, positiveTask, negativeTask)
    }

    private fun insertReminder(year: Int, month: Int,
                               day: Int, hour: Int, minute: Int,
                               date: String, time: String, note: String){
        viewModel.insertReminder(year, month, day, hour, minute, date, time, note)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}