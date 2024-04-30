package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.altoque.R

class IniciarSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciar_sesion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etContra = findViewById<EditText>(R.id.etContra)
        val btIniciarSesion = findViewById<Button>(R.id.btRegistrarme)
        val btCliente = findViewById<Button>(R.id.btCliente)
        val btEspecialista = findViewById<Button>(R.id.btEspecialista)
        val tvRegistrate = findViewById<TextView>(R.id.tvRegistrate)
        var rol = "NOHAYROL"

        btIniciarSesion.setOnClickListener {
            val email = etCorreo.text.toString()
            val contra = etContra.text.toString()

            if (isValidEmail(email)) {
                when (rol) {
                    "ESPECIALISTA" -> {
                        //COLOCAR EL NOMBRE DEL ACTIVITY
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    "CLIENTE" -> {
                        //COLOCAR EL NOMBRE DEL ACTIVITY
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else -> {
                        Toast.makeText(this, "Agrega un rol", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Usuario no Valido", Toast.LENGTH_LONG).show()
            }
        }

        tvRegistrate.setOnClickListener{
            val intent = Intent(this, Registrarse::class.java)
            startActivity(intent)
        }

        btCliente.setOnClickListener{
            rol = "CLIENTE"
        }
        btEspecialista.setOnClickListener{
            rol = "ESPECIALISTA"
        }
    }
    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}