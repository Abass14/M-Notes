package com.example.m_notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.m_notes.R
import com.example.m_notes.model.ArchiveModel
import com.example.m_notes.utils.NoteClickListener
import com.example.m_notes.utils.NoteLongClickListener
import com.google.android.material.switchmaterial.SwitchMaterial

class ArchiveRecyclerViewAdapter(
    val archiveClickListener: NoteClickListener,
    val archiveLongClickListener: NoteLongClickListener
) : RecyclerView.Adapter<ArchiveRecyclerViewAdapter.ArchiveViewHolder>() {
    var archiveList: List<ArchiveModel> = mutableListOf()

    class ArchiveViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.homeNotesTitle)
        val date = view.findViewById<TextView>(R.id.homeNotesDates)

        fun bind(notes: ArchiveModel){
            title.text = notes.title
            date.text = notes.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.home_notes_layout, parent, false)
        return ArchiveViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ArchiveViewHolder, position: Int) {
        holder.bind(archiveList[position])

        holder.itemView.setOnClickListener {
            archiveClickListener.onClick(position)
        }

        holder.itemView.setOnLongClickListener {
            archiveLongClickListener.onLongClick(position)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return archiveList.size
    }

    fun setArchiveNotes(noteList: List<ArchiveModel>) {
        archiveList = noteList
        notifyDataSetChanged()
    }
}