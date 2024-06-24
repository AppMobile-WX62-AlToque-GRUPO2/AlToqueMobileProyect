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
import com.example.altoque.networking.ProfessionService
import com.example.altoque.networking.SharedPreferences
import com.example.altoque.networking.SpecialistService
import com.example.altoque.networking.UbicationService
import com.example.altoque.networking.UserService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShowSpecialistProfileActivity : AppCompatActivity() {
    // IDs de cliente, usuario, ubicación y distrito
    private var clientId = 1
    private var userId = 1
    private var role = 1

    private lateinit var specialistService: SpecialistService
    private lateinit var userService: UserService
    private lateinit var ubicationService: UbicationService
    private lateinit var professionService: ProfessionService

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
        setContentView(R.layout.activity_show_specialist_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editProfile = findViewById<TextView>(R.id.btEditProfileShowSpecialistProfileActivity)
        editProfile.setOnClickListener {
            startActivity(Intent(this, SpecialistProfileActivity::class.java))
        }

        // Llamar al número de teléfono
        val tvPhone = findViewById<TextView>(R.id.tvPhoneResponseShowSpecialistActivity)
        tvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + tvPhone.text)
            startActivity(intent)
        }

        // Ir al correo
        val tvEmail = findViewById<TextView>(R.id.tvEmailResponseShowSpecialistActivity)
        tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:" + tvEmail.text)
            startActivity(intent)
        }

        // Ir a la dirección
        val tvAddress = findViewById<TextView>(R.id.tvAddressResponseShowSpecialistActivity)
        tvAddress.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("geo:0,0?q=" + tvAddress.text)
            startActivity(intent)
        }

        // Regresar
        val btBack = findViewById<ImageButton>(R.id.btBackShowSpecialistProfileActivity)
        btBack.setOnClickListener {
            finish()
            // Regresar al menu del experto
            val intent = Intent(this, MenuExpertActivity::class.java)
            // Limpiar el stack de tareas
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        // Cerrar sesión
        val btLogout = findViewById<Button>(R.id.btLogoutShowSpecialistActivity)
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

        specialistService = retrofit.create(SpecialistService::class.java)
        userService = retrofit.create(UserService::class.java)
        ubicationService = retrofit.create(UbicationService::class.java)
        professionService = retrofit.create(ProfessionService::class.java)

        lifecycleScope.launch {
            try {
                val response = specialistService.getSpecialistIdByUserAndRole(userId, role.toString())
                if (response.isSuccessful) {
                    val specialistIdResponse = response.body()
                    if (specialistIdResponse != null) {
                        clientId = specialistIdResponse.specialist_id

                        // Obtener información del Specialist
                        val specialistResponse = specialistService.getSpecialist(1)

                        // Obtener información de la profesión del especialista
                        val professionId = specialistResponse.professionId
                        val professionResponse = professionService.getProfession(professionId)

                        // Obtener información del usuario utilizando userId del cliente
                        val userId = specialistResponse.userId
                        val userResponse = userService.getUser(userId)

                        // Obtener información de la ubicación del usuario
                        val ubicationId = userResponse.ubicationId
                        val ubicationResponse = ubicationService.getUbication(ubicationId)

                        runOnUiThread {
                            // Actualizar los campos del UI con la información del usuario
                            val tvName = findViewById<TextView>(R.id.tvNameShowSpecialistActivtiy)
                            val tvEmail = findViewById<TextView>(R.id.tvEmailResponseShowSpecialistActivity)
                            val tvPhone = findViewById<TextView>(R.id.tvPhoneResponseShowSpecialistActivity)
                            val tvBirthday = findViewById<TextView>(R.id.tvBirthdayResponseShowSpecialistActivity)
                            val tvDescription = findViewById<TextView>(R.id.tvDescriptionResponseShowSpecialistActivity)
                            val tvAddress = findViewById<TextView>(R.id.tvAddressResponseShowSpecialistActivity)
                            val tvWorkExperience = findViewById<TextView>(R.id.tvWorkExperienceResponseShowSpecialistActivity)
                            val tvSpecialty = findViewById<TextView>(R.id.tvSpecialityShowSpecialistActivtiy)
                            val tvPrice = findViewById<TextView>(R.id.tvPriceResponseShowSpecialistActivity)
                            val ivUser = findViewById<ImageView>(R.id.ivUserShowSpecialistActivity)

                            tvName.setText(userResponse.firstName + " " + userResponse.lastName)
                            tvPhone.setText(userResponse.phone)
                            tvEmail.setText(userResponse.email)
                            tvBirthday.setText(userResponse.birthdate)
                            tvDescription.setText(userResponse.description)
                            tvAddress.setText(ubicationResponse.address)
                            // Se actualiza el campo de experiencia laboral y precio de consulta
                            tvWorkExperience.setText(specialistResponse.workExperience.toString())
                            tvPrice.setText(specialistResponse.consultationPrice.toString())
                            // Se actualiza el campo de especialidad
                            tvSpecialty.setText(professionResponse.name)

                            // Cargar la imagen del usuario desde la URL de la API utilizando Glide
                            if (userResponse.avatar != null) {
                                Glide.with(this@ShowSpecialistProfileActivity)
                                    .load(userResponse.avatar)
                                    .placeholder(R.drawable.default_user)
                                    .into(ivUser)
                            } else {
                                //Si no hay imagen se le da una imagen default
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
                    Toast.makeText(this@ShowSpecialistProfileActivity, "Error al cargar la información", Toast.LENGTH_SHORT).show()
                    Log.e("SpecialistProfileActivity", "Error loading information", e)
                }
            }
        }
    }
}