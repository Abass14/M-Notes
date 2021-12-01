package com.example.m_notes.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m_notes.R
import com.example.m_notes.adapter.ArchiveRecyclerViewAdapter
import com.example.m_notes.databinding.FragmentArchiveBinding
import com.example.m_notes.model.ArchiveModel
import com.example.m_notes.utils.AppSharedPreferences
import com.example.m_notes.utils.Dialog
import com.example.m_notes.utils.NoteClickListener
import com.example.m_notes.utils.NoteLongClickListener
import com.example.m_notes.viewmodel.ApplicationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Archive : Fragment(), NoteClickListener, NoteLongClickListener {
    private var _binding: FragmentArchiveBinding? = null
    private val binding get() = _binding!!
    private var passwordDialog: MaterialAlertDialogBuilder? = null
    private var inputPasswordDialog: AlertDialog? = null
    private val viewModel: ApplicationViewModel by viewModels()
    private lateinit var archivedNotesAdapter: ArchiveRecyclerViewAdapter
    private lateinit var archivedNoteList: List<ArchiveModel>
    private var deleteDialog: MaterialAlertDialogBuilder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        AppSharedPreferences.initPreference(requireActivity())
        val setPasswordPrefValue = AppSharedPreferences.getPasswordPref(AppSharedPreferences.SET_PASSWORD_KEY)
        val setInputPasswordValue = AppSharedPreferences.getInputPasswordPref(AppSharedPreferences.INPUT_PASSWORD_KEY)
        if (setPasswordPrefValue == 0){
            showSetPasswordDialog()
        }else if (setPasswordPrefValue == 2){
            showInputPasswordDialog()
        }
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        archivedNotesAdapter = ArchiveRecyclerViewAdapter(this, this)
        archivedNoteList = listOf()
        setupRecyclerView()
        getArchivedNotes()
        onBackPressed()
    }

    private fun showScreen(){
        if (archivedNotesAdapter.archiveList.isNotEmpty()){
            binding.archiveEmptyScreen.visibility = View.GONE
            binding.archiveRecViewLayout.visibility = View.VISIBLE
        }else{
            binding.archiveEmptyScreen.visibility = View.VISIBLE
            binding.archiveRecViewLayout.visibility = View.GONE
        }
    }

    private fun setupRecyclerView(){
        binding.archiveRecyclerview.apply {
            adapter = archivedNotesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun showSetPasswordDialog(){
        val taskPositive= DialogInterface.OnClickListener { dialogInterface, i ->
            AppSharedPreferences.setPasswordPref(2)
            //TODO
        }
        val taskNegative = DialogInterface.OnClickListener { dialogInterface, _ ->
            AppSharedPreferences.setPasswordPref(1)
            dialogInterface.cancel()
        }
        Dialog.alertDialog(passwordDialog, requireActivity(), requireContext(),getString(R.string.password_archive_note), null,
            "Set Password", "No need", null, R.layout.password_dialog_layout,
            R.style.RoundShapeTheme, taskPositive, taskNegative
        )
    }

    private fun getArchivedNotes() {
        viewModel.allArchivedNoteLiveData.observe(viewLifecycleOwner, Observer {
            archivedNotesAdapter.setArchiveNotes(it.reversed())
            showScreen()
            archivedNoteList = it.reversed()
        })
    }

    private fun deleteArchivedNote(id: Int) {
        viewModel.deleteArchivedNote(id)
    }
    private fun showInputPasswordDialog () {
        inputPasswordDialog = activity.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.apply {
                setView(inflater.inflate(R.layout.input_password_layout, null))
                    .setTitle("Set Password")
                    .setIcon(R.drawable.ic_baseline_lock_24)
                    .setNegativeButton("CANCEL",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.cancel()
                            findNavController().popBackStack()
                        })
            }
            builder.create()
        }
        inputPasswordDialog?.show()
        inputPasswordDialog?.setCanceledOnTouchOutside(false)
    }

    private fun onBackPressed(){
        //Overriding onBack press to finish activity and exit app
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_archive_to_home2)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(position: Int) {
        val note = archivedNoteList[position]
        val action = ArchiveDirections.actionArchiveToArchiveReadEdit(note.id)
        findNavController().navigate(action)
    }

    override fun onLongClick(position: Int) {
        val note = archivedNoteList[position]
        val positiveTask = DialogInterface.OnClickListener { dialogInterface, i ->
            deleteArchivedNote(note.id)
            Log.d("AppViewModel: NoteId", "${note.id}")
            Dialog.toastMsg(requireContext(), "Note Successfully Deleted")
        }
        val negativeTask = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        Dialog.alertDialog(deleteDialog, requireActivity(), requireContext(),
            "Delete Archived Note", "Are you sure you and to delete?", "Yes", "No",
                R.drawable.ic_baseline_delete_24, null, R.style.RoundShapeTheme, positiveTask, negativeTask)

    }
}