package com.example.test2

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class specialist_detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_specialist_detail)

// Obtener datos del intent
        val userName = intent.getStringExtra("USER_NAME")
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userPhone = intent.getStringExtra("USER_PHONE")
        val userImage = intent.getStringExtra("USER_IMAGE")
        val userProfession = intent.getStringExtra("USER_PROFESSION")
        val userDescription = intent.getStringExtra("USER_DESCRIPTION")
        val userWorkExperience = intent.getFloatExtra("USER_WORK_EXPERIENCE", 0.0f)

        val btnBack = findViewById<ImageButton>(R.id.imageButton)

        // Configurar la interfaz de usuario con los datos del especialista
        findViewById<TextView>(R.id.tvEspecialistName).text = userName
        findViewById<TextView>(R.id.tvEspecialistDescription).text = userDescription
        findViewById<TextView>(R.id.tvEspecialistEspacialidad).text = userProfession
        findViewById<TextView>(R.id.tvEspecialistRating).text = "$userWorkExperience /5.0 estrellas"

        Glide.with(this).load(userImage).into(findViewById(R.id.ivEspecialistImage))

        // Configurar botón de regreso
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}