package com.example.m_notes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.m_notes.R
import com.example.m_notes.databinding.FragmentWriteBinding
import com.example.m_notes.viewmodel.ApplicationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class Write : Fragment() {
    private  var _binding: FragmentWriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ApplicationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }

    private fun clickListeners() {
        binding.writeClear.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.writeSave.setOnClickListener {
            val title = binding.writeTitle.text.toString()
            val note = binding.writeEditText.text.toString()
            val calender = Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val date = "$day-$month-$year"
            insertNotes(title, note, date)
            Toast.makeText(requireContext(), "Notes Saved Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_write_to_home2)
        }
    }

    private fun insertNotes(title: String, note: String, date: String){
        viewModel.insertHomeNotes(title, note, date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}