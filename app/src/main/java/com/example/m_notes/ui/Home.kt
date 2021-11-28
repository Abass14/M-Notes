package com.example.m_notes.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m_notes.R
import com.example.m_notes.adapter.NotesRecyclerViewAdapter
import com.example.m_notes.databinding.FragmentHomeBinding
import com.example.m_notes.model.HomeNoteModel
import com.example.m_notes.utils.NoteClickListener
import com.example.m_notes.utils.NoteLongClickListener
import com.example.m_notes.viewmodel.ApplicationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : Fragment(), NoteClickListener, NoteLongClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var exitDialog: AlertDialog? = null
    private val viewModel: ApplicationViewModel by viewModels()
    private lateinit var homeNotesAdapter: NotesRecyclerViewAdapter
    private lateinit var noteList: List<HomeNoteModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeNotesAdapter = NotesRecyclerViewAdapter(this, this)
        noteList = listOf()
        clickListeners()
        setRecyclerView()
        observeAllNotes()
        onBackPressed()
    }

    private fun clickListeners() {
        binding.homeAddNotesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_write)
        }
    }

    private fun createExitDialog(){
        exitDialog = activity.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.apply {
                setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            requireActivity().finish()
                        })
                    .setNegativeButton("No",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.cancel()
                        })
            }
            builder.create()
        }
        exitDialog?.show()
    }

    private fun setRecyclerView() {
        binding.homeRecyclerview.apply {
            adapter = homeNotesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun screenDisplay(){
        if (homeNotesAdapter.notesList.isNotEmpty()){
            binding.homeRecyclerviewLayout.visibility = View.VISIBLE
            binding.homeEmptyScreen.visibility = View.GONE
        }
    }

    private fun observeAllNotes() {
        viewModel.allNotesLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null){
                homeNotesAdapter.setNoteList(it)
                screenDisplay()
                noteList = it
                Toast.makeText(requireContext(), "not empty", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "is empty", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun onBackPressed(){
        //Overriding onBack press to finish activity and exit app
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                createExitDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(position: Int) {
        val note = noteList[position]
            val action = HomeDirections.actionHome2ToHomeReadEdit(note.id)
            findNavController().navigate(action)
    }

    override fun onLongClick(position: Int) {
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
    }
}