package com.example.m_notes.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.m_notes.R
import com.example.m_notes.databinding.FragmentReminderReadEditBinding
import com.example.m_notes.model.ReminderModel
import com.example.m_notes.utils.CurrentDate
import com.example.m_notes.utils.Dialog
import com.example.m_notes.utils.Validations
import com.example.m_notes.viewmodel.ApplicationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderReadEdit : Fragment() {
    private var _binding: FragmentReminderReadEditBinding? = null
    private val binding get() = _binding!!
    private val args: ReminderReadEditArgs by navArgs()
    private lateinit var reminder: ReminderModel
    private var reminderHour: Int? = null
    private var reminderMinute: Int? = null
    private var reminderDay: Int? = null
    private var reminderMonth: Int? = null
    private var reminderYear: Int? = null
    private val successDialog: MaterialAlertDialogBuilder? = null
    private val viewModel: ApplicationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReminderReadEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reminder = args.reminder
        retrievedData()
        reminderTypeSetUp()
        clickListeners()
    }

    private fun reminderTypeSetUp() {
        val reminderType = resources.getStringArray(R.array.reminder_type)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, reminderType)
        binding.reminderAutoCompleteEditTv.setAdapter(arrayAdapter)
    }

    private fun updateReminder(year: Int, month: Int,
                               day: Int, hour: Int, minute: Int,
                               date: String, time: String, note: String, id:Int, isSet: Boolean,
                               showDialog: Int, reminderType: String, reminderPosition: Int){
        viewModel.updateReminder(year, month, day, hour, minute,
            date, time, note, id, isSet, showDialog, reminderType, reminderPosition)
    }
    private fun clickListeners(){
        binding.reminderEditClear.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.reminderEditEdit.setOnClickListener {
            binding.reminderEditEditText.isEnabled = true
        }
        binding.setReminderEditDateBtn.setOnClickListener {
            showDateDialog()
        }
        binding.setReminderEditTimeBtn.setOnClickListener {
            showTimeDialog()
        }
        binding.reminderEditSave.setOnClickListener {
            val reminderType = binding.reminderAutoCompleteEditTv.text
            if (Validations.validateYear(reminderYear) && Validations.validateMonth(reminderMonth) &&
                Validations.validateDay(reminderDay) && Validations.validateHour(reminderHour) &&
                Validations.validateMinute(reminderMinute) &&
                binding.reminderEditEditText.text.isNotEmpty()){
                binding.setDateEditErrorTxt.text = ""
                binding.setTimeEditErrorTxt.text = ""
                binding.reminderTypeEditErrorTxt.text = ""
                if (reminderType.toString() == "Daily"){
                    updateReminder(
                        reminderYear!!, reminderMonth!!,
                        reminderDay!!, reminderHour!!, reminderMinute!!,
                        binding.reminderEditDateTxt.text.toString(),
                        binding.reminderEditTimeTxt.text.toString(),
                        binding.reminderEditEditText.text.toString(),
                        reminder.id,
                        false,
                        0,
                        binding.reminderAutoCompleteEditTv.text.toString(),
                        reminder.reminderPosition
                    )
                    showSuccessDialog()
                    findNavController().popBackStack()
                }else if(reminderType.toString() == "One-time"){
                    if (Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay) &&
                        Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                        updateReminder(
                            reminderYear!!, reminderMonth!!,
                            reminderDay!!, reminderHour!!, reminderMinute!!,
                            binding.reminderEditDateTxt.text.toString(),
                            binding.reminderEditTimeTxt.text.toString(),
                            binding.reminderEditEditText.text.toString(),
                            reminder.id,
                            false,
                            0,
                            binding.reminderAutoCompleteEditTv.text.toString(),
                            reminder.reminderPosition
                        )
                        showSuccessDialog()
                        findNavController().popBackStack()
                    }else{
                        if (!Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                            Dialog.toastMsg(requireContext(), "Invalid Time")
                            binding.setTimeEditErrorTxt.text = "Invalid Time"
                        }
                        if (!Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay)){
                            Dialog.toastMsg(requireContext(), "Invalid Date")
                            binding.setDateEditErrorTxt.text = "Invalid Date"
                        }
                    }
                }else{
                    if (!Validations.validateReminderType(reminderType.toString())){
                        binding.reminderTypeEditErrorTxt.text = "Select type of reminder!!"
                    }
                }
            }else{
                if (!Validations.validateYear(reminderYear)){
                    binding.setDateEditErrorTxt.text = "Date not set!!"
                }else{
                    if (reminderType.toString() == "One-time"){
                        if (!Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                            Dialog.toastMsg(requireContext(), "Invalid Time")
                            binding.setTimeEditErrorTxt.text = "Invalid Time"
                        }
                        if (!Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay)){
                            Dialog.toastMsg(requireContext(), "Invalid Date")
                            binding.setDateEditErrorTxt.text = "Invalid Date"
                        }
                    }else{
                        binding.setDateEditErrorTxt.text = ""
                    }
                }
                if (!Validations.validateMonth(reminderMonth)){
                    binding.setDateEditErrorTxt.text = "Date not set!!"
                }else{
                    if (reminderType.toString() == "One-time"){
                        if (!Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                            Dialog.toastMsg(requireContext(), "Invalid Time")
                            binding.setTimeEditErrorTxt.text = "Invalid Time"
                        }
                        if (!Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay)){
                            Dialog.toastMsg(requireContext(), "Invalid Date")
                            binding.setDateEditErrorTxt.text = "Invalid Date"
                        }
                    }else{
                        binding.setDateEditErrorTxt.text = ""
                    }
                }
                if (!Validations.validateDay(reminderDay)){
                    binding.setDateEditErrorTxt.text = "Date not set!!"
                }else{
                    if (reminderType.toString() == "Daily"){
                        if (!Validations.validateDailyReminderDate(reminderYear)){
                            binding.setDateEditErrorTxt.text = "Invalid Year selection"
                        }
                    }else if (reminderType.toString() == "One-time"){
                        if (!Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                            Dialog.toastMsg(requireContext(), "Invalid Time")
                            binding.setTimeEditErrorTxt.text = "Invalid Time"
                        }
                        if (!Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay)){
                            Dialog.toastMsg(requireContext(), "Invalid Date")
                            binding.setDateEditErrorTxt.text = "Invalid Date"
                        }
                    }else{
                        binding.setDateEditErrorTxt.text = ""
                    }
                }
                if (!Validations.validateHour(reminderHour)){
                    binding.setTimeEditErrorTxt.text = "Time not set!!"
                }else{
                    if (reminderType.toString() == "One-time"){
                        if (!Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                            Dialog.toastMsg(requireContext(), "Invalid Time")
                            binding.setTimeEditErrorTxt.text = "Invalid Time"
                        }
                        if (!Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay)){
                            Dialog.toastMsg(requireContext(), "Invalid Date")
                            binding.setDateEditErrorTxt.text = "Invalid Date"
                        }
                    }else {
                        binding.setTimeEditErrorTxt.text = ""
                    }
                }
                if (!Validations.validateMinute(reminderMinute)){
                    binding.setTimeEditErrorTxt.text = "Time not set!!"
                }else{
                    if (reminderType.toString() == "One-time"){
                        if (!Validations.validateOneTimeReminderTime(reminderHour, reminderMinute)){
                            Dialog.toastMsg(requireContext(), "Invalid Time")
                            binding.setTimeEditErrorTxt.text = "Invalid Time"
                        }
                        if (!Validations.validateOneTimeReminderDate(reminderYear, reminderMonth, reminderDay)){
                            Dialog.toastMsg(requireContext(), "Invalid Date")
                            binding.setDateEditErrorTxt.text = "Invalid Date"
                        }
                    }else {
                        binding.setTimeEditErrorTxt.text = ""
                    }
                }
                if (!Validations.validateReminderType(reminderType.toString())){
                    binding.reminderTypeEditErrorTxt.text = "Select type of reminder!!"
                }else{
                    binding.reminderTypeEditErrorTxt.text = ""
                }
                if (binding.reminderEditEditText.text.isEmpty()) {
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

    private fun retrievedData() {
        binding.reminderEditDateTxt.text = reminder.date
        binding.reminderEditTimeTxt.text = reminder.time
        binding.reminderEditEditText.setText(reminder.note)
        binding.reminderAutoCompleteEditTv.setText(reminder.reminderType)
    }

    private fun showDateDialog(){
        val datePickerDialog = DatePickerDialog(requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                "$dayOfMonth-${monthOfYear + 1}-$year".also { binding.reminderEditDateTxt.text = it }
                reminderMonth = monthOfYear
                reminderYear = year
                reminderDay = dayOfMonth
            }, CurrentDate.year, CurrentDate.month, CurrentDate.day
        )
        datePickerDialog.show()
    }

    private fun showTimeDialog(){
        val timePickerDialog = TimePickerDialog(requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                binding.reminderEditTimeTxt.text = "$hourOfDay:$minute"
                reminderHour = hourOfDay
                reminderMinute = minute
            },
            CurrentDate.hour,
            CurrentDate.minute,
            false
        )
        timePickerDialog.show()
    }

    private fun showSuccessDialog(){
        val positiveTask = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        val negativeTask = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        Dialog.alertDialog(successDialog, requireActivity(), requireContext(),
            "Reminder Updated Successfully!!", "Toggle the Switch button to Schedule or Cancel Reminder",
            "OK", "", null, null, R.style.RoundShapeTheme, positiveTask, negativeTask)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}