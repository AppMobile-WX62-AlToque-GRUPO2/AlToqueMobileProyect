package com.example.altoque

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
import com.example.altoque.IU.MainActivity

class Registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etContra = findViewById<EditText>(R.id.etContra)
        val etRepiteContra = findViewById<EditText>(R.id.etRepiteContra)

        val btRegistrarme = findViewById<Button>(R.id.btRegistrarme)
        val btCliente = findViewById<Button>(R.id.btCliente)
        val btEspecialista = findViewById<Button>(R.id.btEspecialista)

        val tvIniciarSesion = findViewById<TextView>(R.id.tvIniciarSesion)
        var rol = "NOHAYROL"

        btRegistrarme.setOnClickListener {
            val email = etCorreo.text.toString()
            val contra = etContra.text.toString()
            val repiteContra = etRepiteContra.text.toString()

            if (isValidEmail(email)) {
                if (contra == repiteContra) {
                    when (rol) {
                        "ESPECIALISTA" -> {
                            //COLOCAR EL NOMBRE DEL ACTIVITY
                            //val intent = Intent(this, HomeEspecialista::class.java)
                            startActivity(intent)
                        }
                        "CLIENTE" -> {
                            //COLOCAR EL NOMBRE DEL ACTIVITY
                            //val intent = Intent(this, HomeCliente::class.java)
                            startActivity(intent)
                        }
                        else -> {
                            Toast.makeText(this, "Agrega un rol", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Usuario no Válido", Toast.LENGTH_LONG).show()
            }
        }

        tvIniciarSesion.setOnClickListener{
            val intent = Intent(this, IniciarSesion::class.java)
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