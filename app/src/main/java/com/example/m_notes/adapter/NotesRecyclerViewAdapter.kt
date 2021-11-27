package com.example.m_notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.m_notes.R
import com.example.m_notes.model.HomeNoteModel
import com.example.m_notes.utils.NoteClickListener
import com.example.m_notes.utils.NoteLongClickListener

class NotesRecyclerViewAdapter(
    val notesClickListener: NoteClickListener,
    val notesLongClickListener: NoteLongClickListener
    ) : RecyclerView.Adapter<NotesRecyclerViewAdapter.NotesViewHolder>() {
    var notesList: MutableList<HomeNoteModel> = mutableListOf()

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.homeNotesTitle)
        val date = view.findViewById<TextView>(R.id.homeNotesDates)

        fun bind(notes : HomeNoteModel){
            title.text = notes.title
            date.text = notes.date

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.home_notes_layout, parent, false)
        return NotesViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(notesList[position])

        holder.itemView.setOnClickListener {
            notesClickListener.onClick(position)
        }
        holder.itemView.setOnLongClickListener {
            notesLongClickListener.onLongClick(position)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun setNoteList (homeNoteList: MutableList<HomeNoteModel>){
        notesList = homeNoteList
        notifyDataSetChanged()
    }
}