package com.example.m_notes.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m_notes.R
import com.example.m_notes.adapter.ArchiveRecyclerViewAdapter
import com.example.m_notes.databinding.FragmentArchiveBinding
import com.example.m_notes.model.ArchiveModel
import com.example.m_notes.utils.*
import com.example.m_notes.viewmodel.ApplicationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Archive : Fragment(), NoteClickListener, NoteLongClickListener {
    private var _binding: FragmentArchiveBinding? = null
    private val binding get() = _binding!!
    private var passwordDialog: android.app.Dialog? = null
    private var inputPasswordDialog: android.app.Dialog? = null
    private val viewModel: ApplicationViewModel by viewModels()
    private lateinit var archivedNotesAdapter: ArchiveRecyclerViewAdapter
    private lateinit var archivedNoteList: List<ArchiveModel>
    private var deleteDialog: MaterialAlertDialogBuilder? = null
    private var passwordGlobal: String? = null

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
        passwordDialog = android.app.Dialog(requireContext())
        passwordDialog?.setContentView(R.layout.password_dialog_layout)
        onBackPressed()
        val password = passwordDialog?.findViewById<EditText>(R.id.passwordInput)
        passwordGlobal = password?.text.toString()
        val confirmPassword = passwordDialog?.findViewById<EditText>(R.id.confirmPasswordInput)
        val errorTxt = passwordDialog?.findViewById<TextView>(R.id.setPasswordErrorTxt)
        val setPasswordBtn = passwordDialog?.findViewById<AppCompatButton>(R.id.setPasswordBtn)
        val cancel = passwordDialog?.findViewById<AppCompatButton>(R.id.cancelSetBtn)

        password?.addTextChangedListener { editable ->
            if (editable?.length in 1..5){
                errorTxt?.text = "Password has to be up to 6 or more characters"
            }else{
                errorTxt?.setTextColor(resources.getColor(R.color.green_medium))
                errorTxt?.text = "Strong Password"
            }
        }
        setPasswordBtn?.setOnClickListener {
            Dialog.toastMsg(requireContext(), "clicked")
            if (!Validations.validatePasswordStrength(password?.text.toString())){
                errorTxt?.setTextColor(resources.getColor(R.color.red_medium))
                errorTxt?.text = "Password has to be up to 6 or more characters"
            }else if (!Validations.validateSetPasswordEquality(password?.text.toString(), confirmPassword?.text.toString())){
                errorTxt?.setTextColor(resources.getColor(R.color.red_medium))
                errorTxt?.text = "Confirm password not same with Password!!!"
            }else{
                AppSharedPreferences.setPasswordPref(2)
                AppSharedPreferences.setPassword(password?.text.toString())
                passwordDialog?.dismiss()
                Dialog.toastMsg(requireContext(), "Password set successfully")
            }
        }

        cancel?.setOnClickListener {
            AppSharedPreferences.setPasswordPref(1)
            passwordDialog?.dismiss()
        }
        passwordDialog?.show()
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
        inputPasswordDialog = android.app.Dialog(requireContext())
        inputPasswordDialog?.setContentView(R.layout.input_password_layout)
        val inputPassword = inputPasswordDialog?.findViewById<EditText>(R.id.inputPasswordTxt)
        val inputBtn = inputPasswordDialog?.findViewById<AppCompatButton>(R.id.inputPasswordBtn)
        val cancelBtn = inputPasswordDialog?.findViewById<AppCompatButton>(R.id.cancelInputPasswordBtn)
        val errorTxt = inputPasswordDialog?.findViewById<TextView>(R.id.inputPasswordErrorTxt)
        val password = AppSharedPreferences.getPassword(AppSharedPreferences.PASSWORD_KEY)
        inputBtn?.setOnClickListener {
            if (password != null) {
                if (!Validations.validateSetPasswordEquality(
                        inputPassword?.text.toString(),
                        password
                    )
                ) {
                    errorTxt?.text = "Wrong Password"
                } else {
                    Dialog.toastMsg(requireContext(), "Login successful")
                    AppSharedPreferences.setPasswordPref(3)
                    inputPasswordDialog?.dismiss()
                }
            }
        }

        cancelBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_archive_to_home2)
            inputPasswordDialog?.dismiss()
        }



        inputPasswordDialog?.show()
        inputPasswordDialog?.setCancelable(false)
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