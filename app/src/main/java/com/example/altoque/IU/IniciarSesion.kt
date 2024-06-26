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
import com.example.altoque.models.TokenLogin
import com.example.altoque.models.VTokenData
import com.example.altoque.models.VerifyToken
import com.example.altoque.networking.AltoqueService
import com.example.altoque.networking.RetrofitClient.retrofit
import com.example.altoque.networking.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IniciarSesion : AppCompatActivity() {
    lateinit var token : TokenLogin
    lateinit var vTokenData : VTokenData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciar_sesion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btIniciarSesion = findViewById<Button>(R.id.btRegistrarme)
        val btCliente = findViewById<Button>(R.id.btCliente)
        val btEspecialista = findViewById<Button>(R.id.btEspecialista)

        val tvRegistrate = findViewById<TextView>(R.id.tvRegistrate)

        var rol: Boolean? = null

        btIniciarSesion.setOnClickListener {
            validacion_iniciarSesion(rol)
        }

        tvRegistrate.setOnClickListener{
            registrarse()
        }
        btCliente.setOnClickListener{ rol = true }
        btEspecialista.setOnClickListener{ rol = false }
    }
    private fun registrarse() {
        val intent = Intent(this, Registrarse::class.java)
        startActivity(intent)
    }

    private fun validacion_iniciarSesion(rol: Boolean?){
        val etEmail = findViewById<EditText>(R.id.etCorreo)
        val email = etEmail.text.toString()
        val etContra = findViewById<EditText>(R.id.etContra)
        val password = etContra.text.toString()


        // Validación de entradas vacías
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Creamos instancia de retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://altoquebackendapi.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService = retrofit.create(AltoqueService::class.java)
        val loginRequest = Login(email, rol, password) // Suponiendo que Login tiene un constructor que acepta email y password
        val userRequest = userService.postLogin(loginRequest)

        val tokenRequest = userService.getTokenInfo()

        val sharedPreference = SharedPreferences(this)

        userRequest.enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if(response.isSuccessful) {

                    tokenRequest.enqueue(object : Callback<TokenLogin> {
                        override fun onResponse(p0: Call<TokenLogin>, p1: Response<TokenLogin>) {
                            if(p1.isSuccessful) {
                                // TOKEN LOGIN
                                token = p1.body()!!
                                Toast.makeText(this@IniciarSesion, "ACCESS_TOKEN: ${token.access_token}, TYPE:${token.token_type}"
                                    , Toast.LENGTH_SHORT).show()

                                //val verifyTokenRequest = VerifyToken (token.access_token)
                                //val verifyRequest = userService.postVerifyToken(verifyTokenRequest)
                                val fTokenRequest = Login(email, rol, password)
                                val verifyRequest = userService.postToken(fTokenRequest)

                                verifyRequest.enqueue(object :Callback<Login>{
                                    override fun onResponse(p0: Call<Login>, p1: Response<Login>) {
                                        if(p1.isSuccessful) {

                                            val getVerifyRequest = userService.getDataInf()
                                            getVerifyRequest.enqueue(object:Callback<VTokenData>{
                                                override fun onResponse(p0: Call<VTokenData>, p1: Response<VTokenData>) {
                                                    if(p1.isSuccessful) {
                                                        vTokenData = p1.body()!!

                                                        sharedPreference.saveData("UserData", vTokenData)
                                                        val datosUser = sharedPreference.getData("UserData")
                                                        if (datosUser != null) {
                                                            Toast.makeText(this@IniciarSesion, "Role User: ${datosUser.role}, UserID: ${datosUser.id}", Toast.LENGTH_SHORT).show()
                                                        }
                                                        navigateBasedOnRole(rol)
                                                    } else {
                                                        Toast.makeText(this@IniciarSesion, "Arregla tu wnd dedatos", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                                override fun onFailure(p0: Call<VTokenData>, p1: Throwable) {
                                                    TODO("Not yet implemented")
                                                }
                                            })

                                        }else {
                                            Toast.makeText(this@IniciarSesion, "Error al obtener datos de verifyToken ZZZZZZ", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(p0: Call<Login>, p1: Throwable) {
                                        TODO("Not yet implemented")
                                    }
                                })

                            } else { Toast.makeText(this@IniciarSesion, "Error al obtener token", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<TokenLogin>, t: Throwable) {
                            Toast.makeText(this@IniciarSesion, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(this@IniciarSesion, "Error al obtener usuarios", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                Toast.makeText(this@IniciarSesion, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun retorna(rol: Boolean?){
        val etEmail = findViewById<EditText>(R.id.etCorreo)
        val email = etEmail.text.toString()
        val etContra = findViewById<EditText>(R.id.etContra)
        val password = etContra.text.toString()

        val access_token: String
        val token_type: String

        val userService = retrofit.create(AltoqueService::class.java)
        val loginRequest = Login(email, rol, password) // Suponiendo que Login tiene un constructor que acepta email y password
        val userRequest = userService.postLogin(loginRequest)
        loginRequest

    }

    private fun navigateBasedOnRole(rol: Boolean?) {
        when (rol) {
            true -> {
                val intent = Intent(this, MenuCustomerActivity::class.java)
                startActivity(intent)
            }
            false -> {
                val intent = Intent(this, MenuExpertActivity::class.java)
                startActivity(intent)
            }
            else -> {
                Toast.makeText(this, "Por favor seleccione un rol", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
