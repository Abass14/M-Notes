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
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
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
        reminderTypeSetUp()
        clickListeners()
        onBackPressed()
    }

    private fun reminderTypeSetUp() {
        val reminderType = resources.getStringArray(R.array.reminder_type)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, reminderType)
        binding.reminderAutoCompleteTv.setAdapter(arrayAdapter)
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
                false
            )
            timePickerDialog.show()
        }

        binding.reminderSave.setOnClickListener {
            val reminderType = binding.reminderAutoCompleteTv.text
            if (Validations.validateYear(reminderYear) && Validations.validateMonth(reminderMonth) &&
                    Validations.validateDay(reminderDay) && Validations.validateHour(reminderHour) &&
                    Validations.validateMinute(reminderMinute) &&
                    binding.reminderEditText.text.isNotEmpty()){
                binding.setDateErrorTxt.text = ""
                binding.setTimeErrorTxt.text = ""
                binding.reminderTypeErrorTxt.text = ""
                        if (reminderType.toString() == "Daily"){
                            insertReminder(
                                reminderYear!!, reminderMonth!!,
                                reminderDay!!, reminderHour!!, reminderMinute!!,
                                binding.reminderDateTxt.text.toString(),
                                binding.reminderTimeTxt.text.toString(),
                                binding.reminderEditText.text.toString(),
                                binding.reminderAutoCompleteTv.text.toString()
                            )
                            showSuccessDialog()
                            findNavController().popBackStack()
                        }else if(reminderType.toString() == "One-time"){
                            if (Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay) &&
                                    Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                                insertReminder(
                                    reminderYear!!, reminderMonth!!,
                                    reminderDay!!, reminderHour!!, reminderMinute!!,
                                    binding.reminderDateTxt.text.toString(),
                                    binding.reminderTimeTxt.text.toString(),
                                    binding.reminderEditText.text.toString(),
                                    binding.reminderAutoCompleteTv.text.toString()
                                )
                                showSuccessDialog()
                                findNavController().popBackStack()
                            }else{
                                if (!Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                                    Dialog.toastMsg(requireContext(), "Invalid Time")
                                    binding.setTimeErrorTxt.text = "Invalid Time"
                                }
                                if (!Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay)){
                                    Dialog.toastMsg(requireContext(), "Invalid Date")
                                    binding.setDateErrorTxt.text = "Invalid Date"
                                }
                            }
                        }else{
                            if (!Validations.validateReminderType(reminderType.toString())){
                                binding.reminderTypeErrorTxt.text = "Select type of reminder!!"
                            }
                        }
            }else{
                if (!Validations.validateYear(reminderYear)){
                    binding.setDateErrorTxt.text = "Date not set!!"
                }else{
                    if (reminderType.toString() == "One-time"){
                        if (!Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                            Dialog.toastMsg(requireContext(), "Invalid Time")
                            binding.setTimeErrorTxt.text = "Invalid Time"
                        }
                        if (!Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay)){
                            Dialog.toastMsg(requireContext(), "Invalid Date")
                            binding.setDateErrorTxt.text = "Invalid Date"
                        }
                    }else{
                        binding.setDateErrorTxt.text = ""
                    }
                }
                if (!Validations.validateMonth(reminderMonth)){
                    binding.setDateErrorTxt.text = "Date not set!!"
                }else{
                  if (reminderType.toString() == "One-time"){
                        if (!Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                            Dialog.toastMsg(requireContext(), "Invalid Time")
                            binding.setTimeErrorTxt.text = "Invalid Time"
                        }
                        if (!Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay)){
                            Dialog.toastMsg(requireContext(), "Invalid Date")
                            binding.setDateErrorTxt.text = "Invalid Date"
                        }
                    }else{
                        binding.setDateErrorTxt.text = ""
                    }
                }
                if (!Validations.validateDay(reminderDay)){
                    binding.setDateErrorTxt.text = "Date not set!!"
                }else{
                    if (reminderType.toString() == "Daily"){
                        if (!Validations.validateDailyReminderDate(reminderYear)){
                            binding.setDateErrorTxt.text = "Invalid Year selection"
                        }
                    }else if (reminderType.toString() == "One-time"){
                        if (!Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                            Dialog.toastMsg(requireContext(), "Invalid Time")
                            binding.setTimeErrorTxt.text = "Invalid Time"
                        }
                        if (!Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay)){
                            Dialog.toastMsg(requireContext(), "Invalid Date")
                            binding.setDateErrorTxt.text = "Invalid Date"
                        }
                    }else{
                        binding.setDateErrorTxt.text = ""
                    }
                }
                if (!Validations.validateHour(reminderHour)){
                    binding.setTimeErrorTxt.text = "Time not set!!"
                }else{
                    if (reminderType.toString() == "One-time"){
                        if (!Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                            Dialog.toastMsg(requireContext(), "Invalid Time")
                            binding.setTimeErrorTxt.text = "Invalid Time"
                        }
                        if (!Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay)){
                            Dialog.toastMsg(requireContext(), "Invalid Date")
                            binding.setDateErrorTxt.text = "Invalid Date"
                        }
                    }else {
                        binding.setTimeErrorTxt.text = ""
                    }
                }
                if (!Validations.validateMinute(reminderMinute)){
                    binding.setTimeErrorTxt.text = "Time not set!!"
                }else{
                    if (reminderType.toString() == "One-time"){
                        if (!Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                            Dialog.toastMsg(requireContext(), "Invalid Time")
                            binding.setTimeErrorTxt.text = "Invalid Time"
                        }
                        if (!Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay)){
                            Dialog.toastMsg(requireContext(), "Invalid Date")
                            binding.setDateErrorTxt.text = "Invalid Date"
                        }
                    }else {
                        binding.setTimeErrorTxt.text = ""
                    }
                }
                if (!Validations.validateReminderType(reminderType.toString())){
                    binding.reminderTypeErrorTxt.text = "Select type of reminder!!"
                }else{
                    binding.reminderTypeErrorTxt.text = ""
                }
                if (binding.reminderEditText.text.isEmpty()) {
                    val task = DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }
                    Dialog.alertDialog(successDialog, requireActivity(), requireContext(), "No Reminder Note",
                    "Forgot to add a reminder note? Enter a reminder note in the text field below",
                    "OK", "", null, null, R.style.RoundShapeTheme,
                    task, task)
                }
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
                               date: String, time: String, note: String, reminderType: String){
        viewModel.insertReminder(year, month, day, hour, minute, date, time, note, reminderType)
    }

    private fun onBackPressed(){
        //Overriding onBack press to finish activity and exit app
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}