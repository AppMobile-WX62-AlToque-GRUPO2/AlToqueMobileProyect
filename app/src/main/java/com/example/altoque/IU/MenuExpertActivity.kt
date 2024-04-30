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

class MenuExpertActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_expert)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        GoToAgenda()
        GoToProfile()
        GoToServices()
        GoToServices2()
        
    }
    
    private fun GoToAgenda() {
        val llExpAgenda = findViewById<LinearLayout>(R.id.llExpAgenda)
        
        llExpAgenda.setOnClickListener {
            startActivity(Intent(this, AgendaActivity::class.java))
        }
    }
    
    private fun GoToProfile() {
        val llExpMyProfile = findViewById<LinearLayout>(R.id.llExpMyProfile)
        
        llExpMyProfile.setOnClickListener {
            startActivity(Intent(this, AgendaActivity::class.java))
        }
    }
    
    private fun GoToServices() {
        val rvExpServices = findViewById<RecyclerView>(R.id.rvExpServices)
        
        rvExpServices.setOnClickListener {
            startActivity(Intent(this, PublicationActivity::class.java))
        }
    }
    
    private fun GoToServices2() {
        val tvExpServices = findViewById<TextView>(R.id.tvExpServices)
        
        tvExpServices.setOnClickListener {
            startActivity(Intent(this, PublicationActivity::class.java))
        }
    }
    
}