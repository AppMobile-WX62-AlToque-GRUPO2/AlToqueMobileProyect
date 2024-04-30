package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.altoque.R

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        GoToAgenda()
        GoToPublication()
        GoToPublication2()
    }

    private fun GoToAgenda() {
        val llAgenda = findViewById<LinearLayout>(R.id.llAgenda)

        llAgenda.setOnClickListener {
            startActivity(Intent(this, AgendaActivity::class.java))
        }
    }
    private fun GoToPublication() {
        val rvServices = findViewById<RecyclerView>(R.id.rvServices)

        rvServices.setOnClickListener {
            startActivity(Intent(this, PublicationActivity::class.java))
        }
    }
    
    private fun GoToPublication2() {
        val tvServices = findViewById<TextView>(R.id.tvServices)
        
        tvServices.setOnClickListener {
            startActivity(Intent(this, PublicationActivity::class.java))
        }
    }
}