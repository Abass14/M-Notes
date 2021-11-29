package com.example.m_notes.ui

import android.os.Bundle
import android.util.Log
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
import com.example.m_notes.databinding.FragmentHomeReadEditBinding
import com.example.m_notes.model.HomeNoteModel
import com.example.m_notes.utils.CurrentDate
import com.example.m_notes.utils.Dialog
import com.example.m_notes.viewmodel.ApplicationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeReadEdit : Fragment() {
    private var _binding: FragmentHomeReadEditBinding? = null
    private val binding get() = _binding!!
    private val args: HomeReadEditArgs by navArgs()
    private val viewModel: ApplicationViewModel by viewModels()
    private var note: HomeNoteModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeReadEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val noteId = args.id

        getNote(noteId)
        getNoteDetails()
        clickListeners()
    }

    private fun clickListeners () {
        binding.editHomeEdit.setOnClickListener {
            binding.editHomeEditText.isEnabled = true
            binding.editHomeTitle.isEnabled = true
        }
        binding.editHomeClear.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.editHomeSave.setOnClickListener {
            val currentDate = CurrentDate.getCurrentDate()
            if (binding.editHomeTitle.isEnabled) {
                updateNote(
                    binding.editHomeTitle.text.toString(),
                    binding.editHomeEditText.text.toString(),
                    currentDate,
                    args.id
                )
                findNavController().popBackStack()
                Dialog.toastMsg(requireContext(), "Note Updated Successfully")
            }else{
                binding.homeReadEditErrorTxt.text = getString(R.string.note_update_error)
            }
        }
    }


    private fun getNote(id: Int){
        viewModel.getNoteById(id)
    }

    private fun getNoteDetails(){
        viewModel.noteByIdLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null){
                note = it
                binding.editHomeTitle.setText(it.title)
                binding.editHomeEditText.setText(it.note)
                Log.d("AppViewModel: String", it.note)
            }else{
                Log.d("AppViewModel: String2", "failing")
            }
        })

    }

    private fun updateNote(title: String, note: String, date: String, id: Int) {
        viewModel.updateNote(title, note, date, id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}