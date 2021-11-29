package com.example.m_notes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveReadEdit : Fragment() {
    private var _binding: FragmentArchiveReadEditBinding? = null
    private val binding get() = _binding!!
    private val args: ArchiveReadEditArgs by navArgs()
    private val viewModel: ApplicationViewModel by viewModels()
    private var archivedNote: ArchiveModel? = null

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
                Dialog.toastMsg(requireContext(), "Note Updated Successfully")
                findNavController().popBackStack()
            }else{
                binding.archiveWriteErrorTxt.text = getString(R.string.note_update_error)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}