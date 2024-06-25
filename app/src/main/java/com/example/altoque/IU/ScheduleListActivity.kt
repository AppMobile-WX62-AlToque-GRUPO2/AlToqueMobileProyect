package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.altoque.R
import com.example.altoque.adapter.OnItemDeleteClickListener
import com.example.altoque.adapter.ScheduleAdapter
import com.example.altoque.models.PostResponse
import com.example.altoque.models.Schedule
import com.example.altoque.models.ScheduleUpload
import com.example.altoque.networking.ClientService
import com.example.altoque.networking.PostService
import com.example.altoque.networking.ScheduleService
import com.example.altoque.networking.SharedPreferences
import com.example.altoque.repository.ScheduleDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScheduleListActivity : AppCompatActivity(), OnItemDeleteClickListener {
    lateinit var schedules : List<Schedule>
    lateinit var scheduleAdapter : ScheduleAdapter
    private var userId = -1
    private var role = -1
    private var clientId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_schedule_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        //deleteAllSchedules()
        
        val fabAddNewSheduleToList = findViewById<FloatingActionButton>(R.id.fabAddNewSheduleToList)
        fabAddNewSheduleToList.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }
        
        val btUploadAllSchedules = findViewById<Button>(R.id.btUploadAllSchedules)
        btUploadAllSchedules.setOnClickListener {
            // Subir horarios a la API
            uploadSchedules()
            
            val intent = Intent(this, MenuCustomerActivity::class.java)
            startActivity(intent)
        }

        val sharedPreference = SharedPreferences(this)
        val datosUser = sharedPreference.getData("UserData")
        if (datosUser != null) {
            userId = datosUser.id
            role = if (datosUser.role) 1 else 0
        } else {
            Toast.makeText(this, "No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onResume() {
        super.onResume()
        
        loadSchedules()
        
        var rvDateTimesPost = findViewById<RecyclerView>(R.id.rvDateTimesPost)
        
        scheduleAdapter = ScheduleAdapter(schedules, this)
        rvDateTimesPost.adapter = scheduleAdapter
        rvDateTimesPost.layoutManager = LinearLayoutManager(this)
    }
    
    private fun loadSchedules() {
        // Conseguir el ultimo id de los post
        // Obtener todos los schedules con el idpost
        
        schedules = ScheduleDatabase.getInstance(this).getDao().getAll()
    }
    
    private fun deleteAllSchedules() {
        ScheduleDatabase.getInstance(this).getDao().deleteAll()
    }

    private fun uploadSchedules() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://altoquebackendapi.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ScheduleService::class.java)
        val servicePost = retrofit.create(PostService::class.java)
        val serviceClient = retrofit.create(ClientService::class.java)

        lifecycleScope.launch {
            try {
                val clientResponse = serviceClient.getClientIdByUserAndRole(userId, role.toString())
                if (clientResponse.isSuccessful) {
                    val clientIdResponse = clientResponse.body()
                    if (clientIdResponse != null) {
                        clientId = clientIdResponse.client_id
                    }
                } else {
                    Log.e("Upload", "Failed to get clientId: ${clientResponse.errorBody()?.string()}")
                }

                val postResponse = servicePost.getAllPosts()
                if (postResponse.isSuccessful) {
                    val posts = postResponse.body() ?: emptyList()
                    val filteredPosts = posts.filter { it.clientId == clientId }
                    val lastPost = filteredPosts.lastOrNull()

                    if (lastPost != null) {
                        for (schedule in schedules) {
                            val scheduleUpload = ScheduleUpload(
                                schedule.horainicio,
                                schedule.horafin,
                                schedule.fecha,
                                lastPost.id
                            )

                            val request = service.insertSchedule(scheduleUpload)
                            request.enqueue(object : Callback<Schedule> {
                                override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                                    if (response.isSuccessful) {
                                        Log.d("Upload", "Schedule uploaded successfully: ${response.body()}")
                                    } else {
                                        Log.e("Upload", "Failed to upload schedule: ${response.errorBody()?.string()}")
                                    }
                                }

                                override fun onFailure(call: Call<Schedule>, t: Throwable) {
                                    Log.e("Upload", "Failed to upload schedule: ${t.message}")
                                }
                            })
                        }
                        deleteAllSchedules()
                    } else {
                        Log.e("Upload", "No posts found for clientId")
                        Toast.makeText(this@ScheduleListActivity, "No posts found for clientId", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.e("Upload", "Failed to get posts: ${postResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Upload", "Exception: ${e.message}")
                Toast.makeText(this@ScheduleListActivity, "Error uploading schedules", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun reloadActivity() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    override fun onItemClicked(schedule: Schedule) {
        ScheduleDatabase.getInstance(this).getDao().deleteSchedule(schedule)
        finish()
        reloadActivity()
    }

}