package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.altoque.R

class MenuSpecialistActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_specialist)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        GoToAgenda()
        GoToProfile()
        GoToPublication()
        GoToPublication2()
        
    }
    
    private fun GoToAgenda() {
        val llSpcMyAgenda = findViewById<LinearLayout>(R.id.llSpcMyAgenda)
        
        llSpcMyAgenda.setOnClickListener {
            startActivity(Intent(this, AgendaActivity::class.java))
        }
    }
    
    private fun GoToProfile() {
        val llSpcMyProfile = findViewById<LinearLayout>(R.id.llSpcMyProfile)
        
        llSpcMyProfile.setOnClickListener {
            startActivity(Intent(this, AgendaActivity::class.java))
        }
    }
    
    private fun GoToPublication() {
        val rvMyAssigments = findViewById<RecyclerView>(R.id.rvMyAssigments)
        
        rvMyAssigments.setOnClickListener {
            startActivity(Intent(this, PublicationActivity::class.java))
        }
    }
    
    private fun GoToPublication2() {
        val tvServicesRecomendation = findViewById<TextView>(R.id.tvServicesRecomendation)
        
        tvServicesRecomendation.setOnClickListener {
            startActivity(Intent(this, PublicationActivity::class.java))
        }
    }
    
}