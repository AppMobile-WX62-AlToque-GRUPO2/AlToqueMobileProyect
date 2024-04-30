package com.example.altoque.IU

import androidx.fragment.app.FragmentManager
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.altoque.R
import com.example.altoque.repository.PopupFragment


class ClientNotification : AppCompatActivity(), PopupFragment.OnNotificationRejectedListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_notification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fragmentManager: FragmentManager = supportFragmentManager

        findViewById<CardView>(R.id.cvNotification).setOnClickListener {
            showPopup(fragmentManager)
        }

        findViewById<CardView>(R.id.cvNotification2).setOnClickListener {
            showPopup(fragmentManager)
        }

    }

    private fun showPopup(fragmentManager: FragmentManager) {
        val popupFragment = PopupFragment()
        popupFragment.show(fragmentManager, "popup")
    }

    override fun onNotificationRejected() {
        findViewById<CardView>(R.id.cvNotification).visibility = View.GONE
    }

}