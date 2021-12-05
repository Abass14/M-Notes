package com.example.m_notes.ui

import android.content.DialogInterface
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
import com.example.m_notes.utils.CurrentDate
import com.example.m_notes.utils.Dialog
import com.example.m_notes.utils.Validations
import com.example.m_notes.viewmodel.ApplicationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class Write : Fragment() {
    private  var _binding: FragmentWriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ApplicationViewModel by viewModels()
    private var errorTxt: String? = null
    private val dialog: MaterialAlertDialogBuilder? = null

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
            val date = CurrentDate.getCurrentDate()
            if (Validations.validateInsertNote(title, note)){
                insertNotes(title, note, date)
                showSuccessDialog()
                findNavController().navigate(R.id.action_write_to_home2)
            }else{
                showErrorDialog()
            }
        }
    }

    private fun showErrorDialog() {
        val positiveTask = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        errorTxt = getString(R.string.write_error_msg)
        Dialog.alertDialog(dialog, requireActivity(), requireContext(), "Empty fields!!!", errorTxt,
            "OK", "", null, null, R.style.RoundShapeTheme,
            positiveTask, positiveTask)
    }

    private fun showSuccessDialog() {
        val positiveTask = DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        Dialog.alertDialog(dialog, requireActivity(), requireContext(), "Successful!!", "Note Added Successfully! Press OK to continue",
            "OK", "", null, null, R.style.RoundShapeTheme,
            positiveTask, positiveTask)
    }

    private fun insertNotes(title: String, note: String, date: String){
        viewModel.insertHomeNotes(title, note, date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}