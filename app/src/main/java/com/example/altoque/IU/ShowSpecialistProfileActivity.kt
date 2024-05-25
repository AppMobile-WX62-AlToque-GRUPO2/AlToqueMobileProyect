package com.example.altoque.IU

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.altoque.R
import com.example.altoque.networking.ProfessionService
import com.example.altoque.networking.SpecialistService
import com.example.altoque.networking.UbicationService
import com.example.altoque.networking.UserService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShowSpecialistProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
        }

    }

    private fun loadInformation() {
        val urlBase = "https://altoquebackendapi.onrender.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val specialistService = retrofit.create(SpecialistService::class.java)
        val userService = retrofit.create(UserService::class.java)
        val ubicationService = retrofit.create(UbicationService::class.java)
        val professionService = retrofit.create(ProfessionService::class.java)

        lifecycleScope.launch {
            try {
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
                }
            } catch (e: Exception) {
                // Maneja errores
                runOnUiThread {
                    Toast.makeText(this@ShowSpecialistProfileActivity, "Error al cargar la información", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}