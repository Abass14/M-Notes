package com.example.m_notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.m_notes.R
import com.example.m_notes.model.ReminderModel
import com.example.m_notes.utils.NoteLongClickListener

class ReminderRecyclerViewAdapter(
    val reminderLongClickListener: NoteLongClickListener
) : RecyclerView.Adapter<ReminderRecyclerViewAdapter.ReminderViewHolder>() {
    val reminderList: MutableList<ReminderModel> = mutableListOf()

    class ReminderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val date: TextView = view.findViewById(R.id.reminderDate)
        val time: TextView = view.findViewById(R.id.reminderTime)
        val reminders: TextView = view.findViewById(R.id.reminderTitle)

        fun bind(reminder: ReminderModel){
            date.text = reminder.date
            time.text = reminder.time
            reminders.text = reminder.note
        }
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
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }
}