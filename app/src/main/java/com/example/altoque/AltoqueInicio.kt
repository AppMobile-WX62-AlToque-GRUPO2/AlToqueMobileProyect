package com.example.altoque

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.altoque.IU.MainActivity

class AltoqueInicio : AppCompatActivity() {

    private val DURATION: Long = 1500 // Duración del temporizador en milisegundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_altoque_inicio)

        // Inicia el temporizador
        object : CountDownTimer(DURATION, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                val intent: Intent = Intent(this@AltoqueInicio, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()

    }
}