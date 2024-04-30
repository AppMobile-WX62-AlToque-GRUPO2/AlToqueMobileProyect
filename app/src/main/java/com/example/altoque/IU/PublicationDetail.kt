package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.altoque.R

class PublicationDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_publication_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        val ivHorarioImageAddAdd = findViewById<ImageView>(R.id.ivHorarioImageAddAdd)
        ivHorarioImageAddAdd.setOnClickListener{
            redirectToDetailSpecialist()
        }
        
    }
    
    fun redirectToDetailSpecialist(){
        val intent = Intent(this, PublicationDetailEspecialist::class.java)
        startActivity(intent)
    }
    
}