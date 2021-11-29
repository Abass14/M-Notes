package com.example.m_notes.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
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
import com.example.m_notes.adapter.GuideViewPagerAdapter
import com.example.m_notes.adapter.NotesRecyclerViewAdapter
import com.example.m_notes.databinding.FragmentHomeBinding
import com.example.m_notes.model.GuideModel
import com.example.m_notes.model.HomeNoteModel
import com.example.m_notes.utils.AppSharedPreferences
import com.example.m_notes.utils.Dialog
import com.example.m_notes.utils.NoteClickListener
import com.example.m_notes.utils.NoteLongClickListener
import com.example.m_notes.viewmodel.ApplicationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : Fragment(), NoteClickListener, NoteLongClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var exitDialog: MaterialAlertDialogBuilder? = null
    private val viewModel: ApplicationViewModel by viewModels()
    private lateinit var homeNotesAdapter: NotesRecyclerViewAdapter
    private lateinit var noteList: List<HomeNoteModel>
    private var deleteDialog: MaterialAlertDialogBuilder? = null
    private lateinit var guideViewPagerAdapter: GuideViewPagerAdapter

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
        AppSharedPreferences.initPreference(requireActivity())
        AppSharedPreferences.setSplashPref(1)
        homeNotesAdapter = NotesRecyclerViewAdapter(this, this)
        guideViewPagerAdapter = GuideViewPagerAdapter()
        noteList = listOf()
        setupViewPager()
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
        val taskPositive: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialogInterface, i ->
            requireActivity().finish()
        }
        val taskNegative = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.cancel()
        }
        Dialog.alertDialog(exitDialog, requireActivity(), requireContext(),"Exit","Are you sure you want to exit?",
                            "Yes","No",R.drawable.ic_baseline_exit_to_app_24,null, R.style.RoundShapeTheme,
                            taskPositive, taskNegative)
    }

    private fun setupViewPager(){
        binding.homeViewPager.apply {
            adapter = guideViewPagerAdapter
            guideViewPagerAdapter.guideList = GuideModel.guideList
            binding.homeCircularIndicator.setViewPager(binding.homeViewPager)
        }
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
        }else{
            binding.homeEmptyScreen.visibility = View.VISIBLE
            binding.homeRecyclerviewLayout.visibility = View.GONE
        }
    }

    private fun observeAllNotes() {
        viewModel.allNotesLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null){
                homeNotesAdapter.setNoteList(it)
                screenDisplay()
                noteList = it
            }else{
                Toast.makeText(requireContext(), "is empty", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteNotes(id: Int){
        viewModel.deleteNote(id)
    }

    private fun archiveNote(title: String, note: String, date: String) {
        viewModel.insertArchivedNotes(title, note, date)
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
        val note = noteList[position]
        val taskPositive = DialogInterface.OnClickListener { dialogInterface, i ->
            deleteNotes(note.id)
            Dialog.toastMsg(requireContext(), "Note Successfully Deleted!")
        }
        val taskNegative = DialogInterface.OnClickListener { dialogInterface, i ->
            archiveNote(note.title, note.note, note.date)
            deleteNotes(note.id)
            Dialog.toastMsg(requireContext(), "Note Successfully Archived!")
        }

        Dialog.alertDialog(deleteDialog, requireActivity(), requireContext(), "Delete | Archive",
                            "Click on DELETE to delete Note \n\nClick on ARCHIVE to archive Note \n\nTap outside to cancel", "Delete", "Archive",
                            R.drawable.ic_baseline_delete_24, null, R.style.RoundShapeTheme,
                            taskPositive, taskNegative)
    }
}