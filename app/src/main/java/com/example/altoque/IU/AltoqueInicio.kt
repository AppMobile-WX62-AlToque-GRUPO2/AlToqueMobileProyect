package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.altoque.R

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
                val intent: Intent = Intent(this@AltoqueInicio, IniciarSesion::class.java)
                startActivity(intent)
                finish()
            }
        }.start()

    }
}