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
import com.example.altoque.models.Login
import com.example.altoque.models.Register
import com.example.altoque.networking.AltoqueService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        val btRegistrarme = findViewById<Button>(R.id.btRegistrarme)
        val btCliente = findViewById<Button>(R.id.btCliente)
        val btEspecialista = findViewById<Button>(R.id.btEspecialista)

        val tvIniciarSesion = findViewById<TextView>(R.id.tvIniciarSesion)
        var rol: Boolean = true

        btRegistrarme.setOnClickListener {
            registro(rol)
        }

        tvIniciarSesion.setOnClickListener{
            iniciarsesion()
        }

        btCliente.setOnClickListener{rol = true }
        btEspecialista.setOnClickListener{rol = false }

    }

    private fun registro(rol: Boolean) {

        val etEmail = findViewById<EditText>(R.id.etCorreo)
        val email = etEmail.text.toString()

        val etPassword = findViewById<EditText>(R.id.etContra)
        val password = etPassword.text.toString()

        val etRepeatPassword = findViewById<EditText>(R.id.etRepiteContra)
        val repeatPassword = etRepeatPassword.text.toString()

        // Validación de entradas vacías
        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de contraseñas coincidentes
        if (password != repeatPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // Creamos instancia de retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://altoquebackendapi.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService = retrofit.create(AltoqueService::class.java)
        val registerRequest = Register(email, password, rol,
            "Carlos","Onofre","933373674",
            "16-10-2003","aea", "Soy inge de soft",3,1)
        val userRequest = userService.postRegister(registerRequest)


        userRequest.enqueue(object : Callback<Register> {
            override fun onResponse(p0: Call<Register>, response: Response<Register>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Registrarse, "Se creo el usuario correctamente", Toast.LENGTH_SHORT).show()

                } else {
                    /*
                    Toast.makeText(this@Registrarse,
                        "usuario: ${email},password: $password ,rol: $rol" +
                                ",firstname: ${registerRequest.firstName} ,LastName: ${registerRequest.lastName}" +
                                ",phone: ${registerRequest.phone},hb: ${registerRequest.birthdate}" +
                                ",avatar: ${registerRequest.avatar},descrip: ${registerRequest.description} " +
                                ",rating: ${registerRequest.rating},idUbication: ${registerRequest.ubicationId}", Toast.LENGTH_SHORT).show()
                    */
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@Registrarse, "Error al registrar usuario: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Register>, t: Throwable) {
                Toast.makeText(this@Registrarse, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun iniciarsesion() {
        val intent = Intent(this, IniciarSesion::class.java)
        startActivity(intent)
    }
}