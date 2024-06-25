package com.example.altoque.IU

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.altoque.R
import com.example.altoque.models.Contract
import com.example.altoque.networking.ContractService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpecialistDetail : AppCompatActivity() {
    private lateinit var contractService: ContractService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_specialist_detail)

        val userName = intent.getStringExtra("USER_NAME")
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userPhone = intent.getStringExtra("USER_PHONE")
        val userImage = intent.getStringExtra("USER_IMAGE")
        val userProfession = intent.getStringExtra("USER_PROFESSION")
        val userDescription = intent.getStringExtra("USER_DESCRIPTION")
        val userWorkExperience = intent.getFloatExtra("USER_WORK_EXPERIENCE", 0.0f)
        val contractId = intent.getIntExtra("CONTRACT_ID", -1)

        val btnBack = findViewById<ImageButton>(R.id.imageButton)
        val btnAccept = findViewById<Button>(R.id.btnAcceptSpecialist)
        val btnDecline = findViewById<Button>(R.id.btnDeclineSpecialist)

        findViewById<TextView>(R.id.tvEspecialistName).text = userName
        findViewById<TextView>(R.id.tvEspecialistDescription).text = userDescription
        findViewById<TextView>(R.id.tvEspecialistEspacialidad).text = userProfession
        findViewById<TextView>(R.id.tvEspecialistRating).text = "$userWorkExperience /5.0 estrellas"

        Glide.with(this).load(userImage).into(findViewById(R.id.ivEspecialistImage))

        btnBack.setOnClickListener {
            onBackPressed()
        }

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://altoquebackendapi.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        contractService = retrofit.create(ContractService::class.java)

        btnAccept.setOnClickListener {
            updateContractState(contractId, 3)
        }

        btnDecline.setOnClickListener {
            updateContractState(contractId, 2)
        }
    }

    private fun updateContractState(contractId: Int, newState: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val contract = Contract(id = contractId, state = newState, availableDateId = 1, specialistId = 1)
                val response = contractService.updateContractState(contractId, contract).execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@SpecialistDetail, "Contrato actualizado", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@SpecialistDetail, "Ocurrió un error al actualizar el contrato: $errorBody", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SpecialistDetail, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}
