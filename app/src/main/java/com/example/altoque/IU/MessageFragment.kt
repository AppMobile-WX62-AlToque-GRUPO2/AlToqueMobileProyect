package com.example.altoque.IU

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.altoque.R

class MessageFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(R.layout.message_success)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface OnNotificationRejectedListener {
        fun onNotificationRejected()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.message_success, container, false)

        //(activity as? PublicationActivity)?.closeDialog()
        //dismiss()
        return view
    }
}