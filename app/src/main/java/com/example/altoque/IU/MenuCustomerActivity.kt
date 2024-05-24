package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.altoque.R

class MenuCustomerActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_customer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        GoToCreatePost()
        GoToProfile()
        GoToNotifications()
        GoToMyPosts()
        GoToMyPosts2()
    
    }
    
    private fun GoToCreatePost() {
        val llCreatePosts = findViewById<LinearLayout>(R.id.llCustCreatePosts)
        
        llCreatePosts.setOnClickListener {
            startActivity(Intent(this, CustomerCreatePost::class.java))
        }
    }
    
    private fun GoToProfile() {
        val llMyProfile = findViewById<LinearLayout>(R.id.llCustMyProfile)
        
        llMyProfile.setOnClickListener {
            startActivity(Intent(this, clientProfileActivity::class.java))
        }
    }
    
    private fun GoToNotifications() {
        val ibCustNotifications = findViewById<ImageButton>(R.id.ibCustNotifications)
        
        ibCustNotifications.setOnClickListener {
            startActivity(Intent(this, ClientNotification::class.java))
        }
    }
    
    private fun GoToMyPosts() {
        val rvMyPosts = findViewById<RecyclerView>(R.id.rvCustMyPosts)
        
        rvMyPosts.setOnClickListener {
            startActivity(Intent(this, PublicationList::class.java))
        }
    }
    
    private fun GoToMyPosts2() {
        val tvPosts = findViewById<TextView>(R.id.tvCustPosts)
        
        tvPosts.setOnClickListener {
            startActivity(Intent(this, PublicationList::class.java))
        }
    }
    
}