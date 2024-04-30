package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        
        val btToCreate = findViewById<Button>(R.id.btToCreate)
        btToCreate.setOnClickListener{
            redirectToCreationEnvironment()
        }
        
        val btListPost = findViewById<Button>(R.id.btListPost)
        btListPost.setOnClickListener{
            redirectToListPost()
        }
        
        setupViews()
        
    }
    
    fun redirectToCreationEnvironment(){
        val intent = Intent(this, CustomerCreatePost::class.java)
        startActivity(intent)
    }
    
    fun redirectToListPost(){
        val intent = Intent(this, PublicationList::class.java)
        startActivity(intent)
    }
    
    private fun setupViews() {
        val btProfile = findViewById<Button>(R.id.btProfile)
        
        val btProfileSpecialist = findViewById<Button>(R.id.btProfileSpecialist)

        btProfile.setOnClickListener {
            startActivity(Intent(this, clientProfileActivity::class.java))
        }

        btProfileSpecialist.setOnClickListener {
            startActivity(Intent(this, SpecialistProfileActivity::class.java))
        }
    }


}