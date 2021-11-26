package com.example.m_notes.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.m_notes.R
import com.example.m_notes.databinding.FragmentArchiveBinding

class Archive : Fragment() {
    private var _binding: FragmentArchiveBinding? = null
    private val binding get() = _binding!!
    private var passwordDialog: AlertDialog? = null
    private var inputPasswordDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        showSetPasswordDialog()
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showSetPasswordDialog(){
        passwordDialog = activity.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.apply {
                setView(inflater.inflate(R.layout.password_dialog_layout, null))
                    .setNegativeButton("No, I don't need a password",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.cancel()
                        })
            }
            builder.create()
        }
        passwordDialog?.show()
    }

    private fun showInputPasswordDialog () {
        inputPasswordDialog = activity.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.apply {
                setView(inflater.inflate(R.layout.input_password_layout, null))
                    .setTitle("Set Password")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}