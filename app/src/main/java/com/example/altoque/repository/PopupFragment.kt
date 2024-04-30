package com.example.altoque.repository
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.altoque.IU.ClientNotification
import com.example.altoque.R

class PopupFragment: DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(R.layout.popup_layout)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface OnNotificationRejectedListener {
        fun onNotificationRejected()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.popup_layout, container, false)

        view.findViewById<Button>(R.id.btnRechazar).setOnClickListener {
            (activity as? ClientNotification)?.onNotificationRejected()
            dismiss()
        }

        return view
    }

}