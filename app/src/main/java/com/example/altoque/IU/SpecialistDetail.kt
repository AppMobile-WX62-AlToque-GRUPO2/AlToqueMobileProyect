package com.example.altoque.IU

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.altoque.R

class SpecialistDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_specialist_detail)

        val userName = intent.getStringExtra("USER_NAME")
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userPhone = intent.getStringExtra("USER_PHONE")
        val userImage = intent.getStringExtra("USER_IMAGE")
        val userProfession = intent.getStringExtra("USER_PROFESSION")
        val userDescription = intent.getStringExtra("USER_DESCRIPTION")
        val userWorkExperience = intent.getFloatExtra("USER_WORK_EXPERIENCE", 0.0f)

        val btnBack = findViewById<ImageButton>(R.id.imageButton)

        findViewById<TextView>(R.id.tvEspecialistName).text = userName
        findViewById<TextView>(R.id.tvEspecialistDescription).text = userDescription
        findViewById<TextView>(R.id.tvEspecialistEspacialidad).text = userProfession
        findViewById<TextView>(R.id.tvEspecialistRating).text = "$userWorkExperience /5.0 estrellas"

        Glide.with(this).load(userImage).into(findViewById(R.id.ivEspecialistImage))

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}