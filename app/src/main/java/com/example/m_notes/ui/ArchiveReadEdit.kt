package com.example.m_notes.ui

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
import androidx.navigation.fragment.navArgs
import com.example.m_notes.R
import com.example.m_notes.databinding.FragmentArchiveReadEditBinding
import com.example.m_notes.model.ArchiveModel
import com.example.m_notes.utils.CurrentDate
import com.example.m_notes.utils.Dialog
import com.example.m_notes.utils.Validations
import com.example.m_notes.viewmodel.ApplicationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveReadEdit : Fragment() {
    private var _binding: FragmentArchiveReadEditBinding? = null
    private val binding get() = _binding!!
    private val args: ArchiveReadEditArgs by navArgs()
    private val viewModel: ApplicationViewModel by viewModels()
    private var archivedNote: ArchiveModel? = null
    private val dialog: MaterialAlertDialogBuilder? = null
    private var errorTxt: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArchiveReadEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val noteId = args.id
        getArchivedNoteDetails(noteId)
        observeArchivedNoteDetails()
        clickListeners()
        onBackPressed()
    }

    private fun clickListeners() {
        binding.editArchiveClear.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.editArchiveEdit.setOnClickListener {
            binding.editArchiveTitle.isEnabled = true
            binding.editEditText.isEnabled = true
        }

        binding.editArchiveSave.setOnClickListener {
            val title = binding.editArchiveTitle.text.toString()
            val note = binding.editEditText.text.toString()
            val date = CurrentDate.getCurrentDate()
            val id = args.id
            if (binding.editArchiveTitle.isEnabled) {
                updateArchivedNote(title, note, date, id)
                val positiveTask = DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                Dialog.alertDialog(dialog, requireActivity(), requireContext(), "Successful Update!!", "Archived note updated successfully! Press OK to continue",
                    "OK", "", null, null, R.style.RoundShapeTheme,
                    positiveTask, positiveTask)
                findNavController().popBackStack()
            }else{
                errorTxt = getString(R.string.note_update_error)
                val positiveTask = DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                Dialog.alertDialog(dialog, requireActivity(), requireContext(), "No Updates!!", errorTxt,
                    "OK", "", null, null, R.style.RoundShapeTheme,
                    positiveTask, positiveTask)
            }
        }
    }

    private fun getArchivedNoteDetails(id: Int){
        viewModel.getArchivedNoteById(id)
    }

    private fun observeArchivedNoteDetails() {
        viewModel.archivedNoteByIdLiveData?.observe(viewLifecycleOwner, Observer {
            if (it !=null){
                archivedNote = it
                binding.editArchiveTitle.setText(it.title)
                binding.editEditText.setText(it.note)
            }
        })
    }

    private fun updateArchivedNote(title: String, note: String, date: String, id: Int){
        viewModel.updateArchivedNote(title, note, date, id)
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