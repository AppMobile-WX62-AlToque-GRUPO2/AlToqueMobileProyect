package com.example.altoque.IU

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.altoque.R
import com.example.altoque.models.ClientIdResponse
import com.example.altoque.models.Login
import com.example.altoque.networking.ClientService
import com.example.altoque.networking.SharedPreferences
import com.example.altoque.networking.UbicationService
import com.example.altoque.networking.UserService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShowClientProfileActivity : AppCompatActivity() {
    // IDs de cliente, usuario, ubicación y distrito
    private var clientId = 1
    private var userId = 1
    private var role = 1

    private lateinit var clientService: ClientService
    private lateinit var userService: UserService
    private lateinit var ubicationService: UbicationService

    override fun onCreate(savedInstanceState: Bundle?) {
        //cargar las preferencias
        val sharedPreference = SharedPreferences(this)
        val datosUser = sharedPreference.getData("UserData")
        if (datosUser != null) {
            userId = datosUser.id
            role = if (datosUser.role) 1 else 0
        } else {
            Toast.makeText(this, "No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show()
        }
        
        loadInformation()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_client_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btEditProfile = findViewById<TextView>(R.id.btEditProfileShowClientProfileActivity)
        btEditProfile.setOnClickListener {
            startActivity(Intent(this, clientProfileActivity::class.java))
        }

        val tvPhone = findViewById<TextView>(R.id.tvPhoneResponseShowClientActivity)

        // Llamar al número de teléfono
        tvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + tvPhone.text)
            startActivity(intent)
        }

        // Ir al correo
        val tvEmail = findViewById<TextView>(R.id.tvEmailResponseShowClientActivity)
        tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:" + tvEmail.text)
            startActivity(intent)
        }

        // Ir a la dirección
        val tvAddress = findViewById<TextView>(R.id.tvAddressResponseShowClientActivity)
        tvAddress.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("geo:0,0?q=" + tvAddress.text)
            startActivity(intent)
        }

        // Regresar
        val btBack = findViewById<ImageButton>(R.id.btBackShowClientProfileActivity)
        btBack.setOnClickListener {
            // Regresar al menu del cliente
            val intent = Intent(this, MenuCustomerActivity::class.java)
            // Limpiar el stack de tareas
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        // Cerrar sesión
        val btLogout = findViewById<Button>(R.id.btLogoutShowClientActivity)
        btLogout.setOnClickListener {
            val intent = Intent(this, IniciarSesion::class.java)
            // Limpiar el stack de tareas
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun loadInformation() {
        val urlBase = "https://altoquebackendapi.onrender.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        clientService = retrofit.create(ClientService::class.java)
        userService = retrofit.create(UserService::class.java)
        ubicationService = retrofit.create(UbicationService::class.java)

        lifecycleScope.launch {
            try {
                val response = clientService.getClientIdByUserAndRole(userId, role.toString())
                if (response.isSuccessful) {
                    val clientIdResponse = response.body()
                    if (clientIdResponse != null) {
                        clientId = clientIdResponse.client_id

                        // Obtener información del cliente
                        val clientResponse = clientService.getClient(clientId)

                        // Obtener información del usuario utilizando userId del cliente
                        val userId = clientResponse.userId
                        val userResponse = userService.getUser(userId)

                        // Obtener información de la ubicación del usuario
                        val ubicationId = userResponse.ubicationId
                        val ubicationResponse = ubicationService.getUbication(ubicationId)

                        runOnUiThread {
                            // Actualizar los campos del UI con la información del usuario
                            val tvName = findViewById<TextView>(R.id.tvNameShowClientActivtiy)
                            val tvEmail = findViewById<TextView>(R.id.tvEmailResponseShowClientActivity)
                            val tvPhone = findViewById<TextView>(R.id.tvPhoneResponseShowClientActivity)
                            val tvBirthday = findViewById<TextView>(R.id.tvBirthdayResponseShowClientActivity)
                            val tvDescription = findViewById<TextView>(R.id.tvDescriptionResponseShowClientActivity)
                            val tvAddress = findViewById<TextView>(R.id.tvAddressResponseShowClientActivity)
                            val ivUser = findViewById<ImageView>(R.id.ivUserShowClientActivity)

                            tvName.text = "${userResponse.firstName} ${userResponse.lastName}"
                            tvPhone.text = userResponse.phone
                            tvEmail.text = userResponse.email
                            tvBirthday.text = userResponse.birthdate
                            tvDescription.text = userResponse.description
                            tvAddress.text = ubicationResponse.address

                            // Cargar la imagen del usuario desde la URL de la API utilizando Glide
                            if (userResponse.avatar != null) {
                                Glide.with(this@ShowClientProfileActivity)
                                    .load(userResponse.avatar)
                                    .placeholder(R.drawable.default_user)
                                    .into(ivUser)
                            } else {
                                // Si no hay imagen, mostrar imagen default
                                ivUser.setImageResource(R.drawable.default_user)
                            }
                        }
                    } else {
                        throw Exception("Client ID response body is null")
                    }
                } else {
                    throw Exception("Failed to fetch client ID: ${response.code()}")
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@ShowClientProfileActivity, "Error al cargar la información: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("ClientProfileActivity", "Error loading information", e)
                }
            }
        }
    }
}