package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.altoque.R

class PublicationList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_publication_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        val btVerMas = findViewById<Button>(R.id.btVerMas)
        btVerMas.setOnClickListener{
            redirectToDetailPost()
        }
        
    }
    
    fun redirectToDetailPost(){
        val intent = Intent(this, PublicationDetail::class.java)
        startActivity(intent)
    }
    
}