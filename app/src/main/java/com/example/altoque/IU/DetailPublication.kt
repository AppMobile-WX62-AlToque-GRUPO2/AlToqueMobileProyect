package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.altoque.R
import com.example.altoque.models.AvailableDate
import com.example.altoque.models.Profession
import com.example.altoque.models.Specialist
import com.example.altoque.models.User
import com.example.altoque.models.Contract
import com.example.altoque.networking.AvailableDateService
import com.example.altoque.networking.ContractService
import com.example.altoque.networking.ProfessionService
import com.example.altoque.networking.SpecialistService
import com.example.altoque.networking.UserService
import com.example.altoque.adapter.SpecialistAdapter
import com.example.altoque.adapter.OnSpecialistClickListener
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class DetailPublication : AppCompatActivity(), OnSpecialistClickListener {
    lateinit var professionalAdapter: SpecialistAdapter
    lateinit var rvProfessionals: RecyclerView
    var professionals: MutableList<User> = mutableListOf()
    var userProfessionMap: MutableMap<Int, String> = mutableMapOf()
    var specialistWorkExperienceMap: MutableMap<Int, Float> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_publication)

        findViewById<ImageButton>(R.id.ibDetailPublicationBack).setOnClickListener {
            onBackPressed()
        }
        findViewById<Button>(R.id.btnDetailPublicationCancelar).setOnClickListener {
            onBackPressed()
        }

        val title = intent.getStringExtra("TITLE") ?: "N/A"
        val address = intent.getStringExtra("ADDRESS") ?: "N/A"
        val description = intent.getStringExtra("DESCRIPTION") ?: "N/A"
        val image = intent.getStringExtra("IMAGE") ?: "N/A"

        findViewById<TextView>(R.id.tvDetailPublicationTittle).text = title
        findViewById<TextView>(R.id.tvDetailPublicationAddress).text = address
        findViewById<TextView>(R.id.tvDetailPublicationDescription).text = description
        Glide.with(this).load(image).into(findViewById(R.id.ivDetailPublication))

        rvProfessionals = findViewById(R.id.rvProfesionalesList)
        rvProfessionals.layoutManager = LinearLayoutManager(this)

        loadProfessionals()
    }

    private fun loadProfessionals() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://altoquebackendapi.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val contractService = retrofit.create(ContractService::class.java)
        val availableDateService = retrofit.create(AvailableDateService::class.java)
        val specialistService = retrofit.create(SpecialistService::class.java)
        val userService = retrofit.create(UserService::class.java)
        val professionService = retrofit.create(ProfessionService::class.java)

        val postId = intent.getIntExtra("POST_ID", -1)

        if (postId != -1) {
            contractService.getContractsByState(1).enqueue(object : Callback<List<Contract>> {
                override fun onResponse(call: Call<List<Contract>>, response: Response<List<Contract>>) {
                    if (response.isSuccessful) {
                        val contracts = response.body()?.filter { it.state == 1 } ?: emptyList()

                        contracts.forEach { contract ->
                            availableDateService.getAvailableDateById(contract.availableDateId).enqueue(object : Callback<AvailableDate> {
                                override fun onResponse(call: Call<AvailableDate>, response: Response<AvailableDate>) {
                                    if (response.isSuccessful) {
                                        val availableDate = response.body()
                                        if (availableDate?.postId == postId) {
                                            specialistService.getSpecialistById(contract.specialistId).enqueue(object : Callback<Specialist> {
                                                override fun onResponse(call: Call<Specialist>, response: Response<Specialist>) {
                                                    if (response.isSuccessful) {
                                                        val specialist = response.body()
                                                        specialist?.let {
                                                            userService.getUserById(it.userId).enqueue(object : Callback<User> {
                                                                override fun onResponse(call: Call<User>, response: Response<User>) {
                                                                    if (response.isSuccessful) {
                                                                        val user = response.body()
                                                                        user?.let {
                                                                            if (!userProfessionMap.containsKey(it.id)) {
                                                                                professionService.getProfessionById(specialist.professionId).enqueue(object : Callback<Profession> {
                                                                                    override fun onResponse(call: Call<Profession>, response: Response<Profession>) {
                                                                                        if (response.isSuccessful) {
                                                                                            val profession = response.body()
                                                                                            profession?.let {
                                                                                                userProfessionMap[user.id] = it.name
                                                                                                specialistWorkExperienceMap[user.id] = specialist.workExperience
                                                                                                professionals.add(user)
                                                                                                professionalAdapter.notifyDataSetChanged()
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                    override fun onFailure(call: Call<Profession>, t: Throwable) {
                                                                                        Log.e("DetailPublication", "Profession request failed", t)
                                                                                    }
                                                                                })
                                                                            } else {
                                                                                specialistWorkExperienceMap[user.id] = specialist.workExperience
                                                                                professionals.add(user)
                                                                                professionalAdapter.notifyDataSetChanged()
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                override fun onFailure(call: Call<User>, t: Throwable) {
                                                                    Log.e("DetailPublication", "User request failed", t)
                                                                }
                                                            })
                                                        }
                                                    }
                                                }

                                                override fun onFailure(call: Call<Specialist>, t: Throwable) {
                                                    Log.e("DetailPublication", "Specialist request failed", t)
                                                }
                                            })
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<AvailableDate>, t: Throwable) {
                                    Log.e("DetailPublication", "AvailableDate request failed", t)
                                }
                            })
                        }
                    } else {
                        Log.e("DetailPublication", "Error in response: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<Contract>>, t: Throwable) {
                    Log.e("DetailPublication", "Request failed", t)
                }
            })
        } else {
            Log.e("DetailPublication", "Invalid postId: $postId")
        }

        professionalAdapter = SpecialistAdapter(professionals, this@DetailPublication)
        rvProfessionals.adapter = professionalAdapter
    }

    override fun onAcceptClicked(especialist: User) {
        Log.d("DetailPublication", "Especialist accepted: ${especialist.firstName} ${especialist.lastName}")
    }

    override fun onDeleteClicked(especialist: User) {
        Log.d("DetailPublication", "Especialist deleted: ${especialist.firstName} ${especialist.lastName}")
    }

    override fun onSpecialistClicked(especialist: User) {
        val profession = userProfessionMap[especialist.id] ?: "N/A"
        val workExperience = specialistWorkExperienceMap[especialist.id] ?: 0f
        val intent = Intent(this, SpecialistDetail::class.java).apply {
            putExtra("USER_NAME", "${especialist.firstName} ${especialist.lastName}")
            putExtra("USER_EMAIL", especialist.email)
            putExtra("USER_PHONE", especialist.phone)
            putExtra("USER_IMAGE", especialist.avatar)
            putExtra("USER_DESCRIPTION", especialist.description)
            putExtra("USER_PROFESSION", profession)
            putExtra("USER_WORK_EXPERIENCE", workExperience)
        }
        startActivity(intent)
    }
}