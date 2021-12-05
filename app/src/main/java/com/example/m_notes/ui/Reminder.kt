package com.example.m_notes.ui

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.GregorianCalendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.m_notes.R
import com.example.m_notes.adapter.ReminderRecyclerViewAdapter
import com.example.m_notes.databinding.FragmentReminderBinding
import com.example.m_notes.model.ReminderModel
import com.example.m_notes.utils.*
import com.example.m_notes.utils.Reminder
import com.example.m_notes.viewmodel.ApplicationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class Reminder : Fragment(), NoteClickListener, NoteLongClickListener, ReminderRecyclerViewAdapter.ReminderSwitchListener {
    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ApplicationViewModel by viewModels()
    private lateinit var reminderList: List<ReminderModel>
    private lateinit var reminderRecyclerViewAdapter: ReminderRecyclerViewAdapter
    private val deleteDialog: MaterialAlertDialogBuilder? = null
    private lateinit var notificationManager: NotificationManager
    private var alarmManager: Array<AlarmManager?>? = null
    val alarmIntentArray: ArrayList<PendingIntent> = arrayListOf()
    private lateinit var alarmIntent: PendingIntent
    private var cancelPref: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Reminder.createNotificationChannel(notificationManager, PRIMARY_CHANNEL_ID)
        reminderList = listOf()
        alarmManager = arrayOfNulls<AlarmManager?>(1000000)
        cancelPref = AppSharedPreferences.getCancelDialogPref(AppSharedPreferences.CANCEL_DIALOG_KEY)
        AppSharedPreferences.initPreference(requireActivity())
        reminderRecyclerViewAdapter = ReminderRecyclerViewAdapter(this, this, this)
        setupRecyclerView()
        clickListeners()
        onBackPressed()
        getReminders()
    }

    private fun showScreen () {
        if (reminderRecyclerViewAdapter.reminderList.isNotEmpty()){
            binding.reminderRecViewLayout.visibility = View.VISIBLE
            binding.reminderEmptyScreen.visibility = View.GONE
        }else{
            binding.reminderRecViewLayout.visibility = View.GONE
            binding.reminderEmptyScreen.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView(){
        binding.reminderRecyclerview.apply {
            adapter = reminderRecyclerViewAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun clickListeners() {
        binding.setReminderBtn.setOnClickListener {
            findNavController().navigate(R.id.action_reminder_to_writeReminder)
        }
    }

    private fun deleteReminder(id: Int){
        viewModel.deleteReminder(id)
    }
    private fun getReminders(){
        viewModel.allReminderLiveData?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null){
                reminderRecyclerViewAdapter.setRemList(it.reversed())
                showScreen()
                reminderList = it.reversed()
            }
        })
    }

    private fun updateIsSetReminder(isSet: Boolean, id: Int){
        viewModel.updateIsSetReminder(isSet, id)
    }

    private fun updateShowDialog(showDialog: Int, id: Int){
        viewModel.updateShowDialog(showDialog, id)
    }

    private fun onBackPressed(){
        //Overriding onBack press to finish activity and exit app
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_reminder_to_home2)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setAlarm(position: Int){
        val reminder = reminderList[position]
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra("Description", reminder.note)
        }
        val notifyPendingIntent = PendingIntent.getBroadcast(requireContext(),
            position, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        alarmIntent = notifyPendingIntent
        val calender = GregorianCalendar(reminder.year, reminder.month,
            reminder.day, reminder.hour, reminder.minute)
        alarmManager!![position] = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (reminder.year >= CurrentDate.year && reminder.day >= CurrentDate.day
                && reminder.month >= CurrentDate.month && reminder.hour >= CurrentDate.hour
                && reminder.minute > CurrentDate.minute) {
                alarmManager!![position]?.setExact(
                    AlarmManager.RTC,
                    calender.timeInMillis, alarmIntent
                )
                showSuccessDialog(position)
                alarmIntentArray.add(alarmIntent)
            }else{
                Dialog.toastMsg(requireContext(), "Reminder can't be ste on Invalid Date or Time")
            }
        }else {
            if (reminder.year >= CurrentDate.year && reminder.day >= CurrentDate.day
                && reminder.month >= CurrentDate.month && reminder.hour >= CurrentDate.hour
                && reminder.minute > CurrentDate.minute) {
                alarmManager!![position]?.set(
                    AlarmManager.RTC,
                    calender.timeInMillis, alarmIntent
                )
                showSuccessDialog(position)
                alarmIntentArray.add(alarmIntent)
            }else{
                Dialog.toastMsg(requireContext(), "Reminder can't be ste on Invalid Date or Time")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setRepeatingAlarm(position: Int) {
        val reminder = reminderList[position]
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra("Description", reminder.note)
        }
        val notifyPendingIntent = PendingIntent.getBroadcast(requireContext(),
            position, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        alarmIntent = notifyPendingIntent
        val calender = GregorianCalendar(reminder.year, reminder.month,
            reminder.day, reminder.hour, reminder.minute)
        alarmManager!![position] = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            if (reminder.year >= CurrentDate.year && reminder.day >= CurrentDate.day
                && reminder.month >= CurrentDate.month) {
                alarmManager!![position]?.setInexactRepeating(
                    AlarmManager.RTC,
                    calender.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    alarmIntent
                )
                showSuccessDialog(position)
                alarmIntentArray.add(alarmIntent)
            }else{
                Dialog.toastMsg(requireContext(), "Reminder can't be set on Invalid Date")
            }
        }else{
            if (reminder.year >= CurrentDate.year && reminder.day >= CurrentDate.day
                && reminder.month >= CurrentDate.month) {
                alarmManager!![position]?.setInexactRepeating(
                    AlarmManager.RTC,
                    calender.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    alarmIntent
                )
                showSuccessDialog(position)
                alarmIntentArray.add(alarmIntent)
            }else{
                Dialog.toastMsg(requireContext(), "Reminder can't be set on Invalid Date")
            }
        }
    }

    private fun cancelAlarm(position: Int){
        val reminder = reminderList[position]
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra("Description", reminder.note)
        }
        val notifyPendingIntent = PendingIntent.getBroadcast(requireContext(),
            position, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        alarmIntent = notifyPendingIntent
        alarmManager!![position] = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager!![position]?.cancel(alarmIntent)
        if (alarmIntentArray.contains(alarmIntent)) {
            alarmIntentArray.remove(alarmIntent)
            showCancelDialog()
        }else{
            Dialog.toastMsg(requireContext(), "Reminder has not been set")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(position: Int) {
        Dialog.toastMsg(requireContext(), "Clicked")
    }

    override fun onLongClick(position: Int) {
        val reminder = reminderList[position]
        val positiveTask = DialogInterface.OnClickListener { dialogInterface, i ->
            deleteReminder(reminder.id)
            Dialog.toastMsg(requireContext(), "Reminder Deleted Successfully!!!")
        }
        val negativeTask = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        Dialog.alertDialog(deleteDialog, requireActivity(), requireContext(), "Delete Scheduled Reminder",
        "Are you sure you want to delete scheduled reminder?", "YES", "NO",
        null, null, R.style.RoundShapeTheme, positiveTask, negativeTask)
    }

    private fun showSuccessDialog(position: Int){
        val reminder = reminderList[position]
        val positiveTask = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        val negativeTask = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        Dialog.alertDialog(deleteDialog, requireActivity(), requireContext(), "Reminder Scheduled for ${reminder.date} Successfully!!",
        "Press OK to Continue", "OK", "", null, null,
        R.style.RoundShapeTheme, positiveTask, negativeTask)
    }

    private fun showCancelDialog(){
        val positiveTask = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        val negativeTask = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        Dialog.alertDialog(deleteDialog, requireActivity(), requireContext(), "Reminder Canceled Successfully!!",
            "Press OK to Continue", "OK", "", null, null,
            R.style.RoundShapeTheme, positiveTask, negativeTask)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showReminderOptionDialog(position: Int){
        val positiveTask = DialogInterface.OnClickListener { dialogInterface, i ->
            setAlarm(position)
        }
        val negativeTask = DialogInterface.OnClickListener { dialogInterface, i ->
            setRepeatingAlarm(position)
        }
        Dialog.alertDialog(deleteDialog, requireActivity(), requireContext(), "Select Reminder Type",
            "Select ONE TIME for one time Reminder and DAILY for Daily Reminder",
            "ONE-TIME", "DAILY", null, null,
            R.style.RoundShapeTheme, positiveTask, negativeTask)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onSwitch(position: Int, isChecked: Boolean) {
        val reminder = reminderList[position]
        if (!isChecked) {
            //FALSE -> CANCEL ALARM HERE
            if (reminder.showDialog == 1) {
                updateIsSetReminder(false, reminder.id)
                cancelAlarm(position)
            }else{
                Dialog.toastMsg(requireContext(), "cancelled")
            }
            Dialog.toastMsg(requireContext(), "FIRST -> isChecked: ${isChecked} isSet -> ${reminder.isSet} $position ")
        }
        else{
            //TRUE -> SET ALARM HERE
            updateShowDialog(1, reminder.id)
            updateIsSetReminder(true, reminder.id)
            showReminderOptionDialog(position)
            Dialog.toastMsg(requireContext(), "SECOND -> isChecked: ${isChecked} isSet -> ${reminder.isSet} $position")
        }
    }

}