package com.example.m_notes.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.widget.Toast
import com.example.m_notes.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Dialog {

    fun toastMsg(context: Context, msg: String) {
        return Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun alertDialog(dialog: MaterialAlertDialogBuilder?,
                    activity: Activity,
                    context: Context,
                    title: String,
                    msg: String?,
                    positiveBtn: String,
                    negativeBtn: String,
                    icon: Int?,
                    view: Int?,
                    theme: Int,
                    taskPositive: DialogInterface.OnClickListener,
                    taskNegative: DialogInterface.OnClickListener,
    ){
        var alertDialog = dialog
        val inflater = activity.layoutInflater
        val dialogBackground = activity.resources.getDrawable(R.drawable.dialog_layout)
        alertDialog = MaterialAlertDialogBuilder(context, theme)
        alertDialog.apply {
            if (icon == null && view == null) {
                this.setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(positiveBtn, taskPositive)
                    .setNeutralButton(negativeBtn, taskNegative).background = dialogBackground
            }else if (icon == null && view != null) {
                this.setTitle(title)
                    .setMessage(msg)
                    .setView(inflater.inflate(view, null))
                    .setPositiveButton(positiveBtn, taskPositive)
                    .setNeutralButton(negativeBtn, taskNegative).background = dialogBackground
            }else if (icon != null && view == null) {
                this.setTitle(title)
                    .setMessage(msg)
                    .setIcon(icon)
                    .setPositiveButton(positiveBtn, taskPositive)
                    .setNeutralButton(negativeBtn, taskNegative).background = dialogBackground
            }else if (msg == null && icon != null && view != null){
                this.setTitle(title)
                    .setIcon(icon)
                    .setView(inflater.inflate(view, null))
                    .setPositiveButton(positiveBtn, taskPositive)
                    .setNeutralButton(negativeBtn, taskNegative).background = dialogBackground
            }
        }
        alertDialog.show()
    }
}