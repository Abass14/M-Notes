package com.example.m_notes.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.m_notes.R
import com.example.m_notes.model.ReminderModel
import com.example.m_notes.utils.AppSharedPreferences
import com.example.m_notes.utils.NoteClickListener
import com.example.m_notes.utils.NoteLongClickListener
import com.google.android.material.switchmaterial.SwitchMaterial

class ReminderRecyclerViewAdapter(
    val reminderLongClickListener: NoteLongClickListener,
    val reminderClickListener: NoteClickListener,
    val reminderSwitchListener: ReminderSwitchListener
) : RecyclerView.Adapter<ReminderRecyclerViewAdapter.ReminderViewHolder>() {
    var reminderList: List<ReminderModel> = mutableListOf()

    class ReminderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val date: TextView = view.findViewById(R.id.reminderDate)
        val time: TextView = view.findViewById(R.id.reminderTime)
        val reminders: TextView = view.findViewById(R.id.reminderTitle)
        val switch = view.findViewById<SwitchMaterial>(R.id.setReminder)

        fun bind(reminder: ReminderModel){
            date.text = reminder.date
            time.text = reminder.time
            reminders.text = reminder.note
            switch.isChecked = reminder.isSet
        }
    }

    interface ReminderSwitchListener{
        fun onSwitch(position: Int, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.reminder_layout, parent, false)
        return ReminderViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(reminderList[position])

        holder.itemView.setOnLongClickListener {
            reminderLongClickListener.onLongClick(position)
            return@setOnLongClickListener true
        }

        holder.itemView.setOnClickListener {
            reminderClickListener.onClick(position)
        }

        holder.switch.setOnClickListener {
            reminderSwitchListener.onSwitch(position, holder.switch.isChecked)
//            Log.d("AppViewModel: Switch", "${compoundButton.isChecked}")
        }
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    fun setRemList (remList: List<ReminderModel>) {
        reminderList = remList
        notifyDataSetChanged()
    }
}