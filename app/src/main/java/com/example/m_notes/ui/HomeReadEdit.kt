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
import androidx.navigation.fragment.navArgs
import com.example.m_notes.R
import com.example.m_notes.databinding.FragmentHomeReadEditBinding
import com.example.m_notes.model.HomeNoteModel
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
    }

    private fun getNote(id: Int){
        viewModel.getNoteById(id)
    }

    private fun getNoteDetails(){
        viewModel.noteByIdLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null){
                note = it
                var title = binding.editHomeTitle
                val noteItem = binding.editHomeEditText
                binding.editHomeTitle.setText(it.title)
                binding.editHomeEditText.setText(it.note)
                Log.d("AppViewModel: String", it.note)
            }else{
                Log.d("AppViewModel: String2", "failing")
                Toast.makeText(requireContext(), "null", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}