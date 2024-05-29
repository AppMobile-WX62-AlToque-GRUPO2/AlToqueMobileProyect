package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.altoque.R
import com.example.altoque.adapter.ScheduleAdapter
import com.example.altoque.models.Schedule
import com.example.altoque.models.ScheduleUpload
import com.example.altoque.networking.ScheduleService
import com.example.altoque.repository.ScheduleDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScheduleListActivity : AppCompatActivity() {
    lateinit var schedules : List<Schedule>
    lateinit var scheduleAdapter : ScheduleAdapter
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
    }
    
    override fun onResume() {
        super.onResume()
        
        loadSchedules()
        
        var rvDateTimesPost = findViewById<RecyclerView>(R.id.rvDateTimesPost)
        
        scheduleAdapter = ScheduleAdapter(schedules)
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
        
        // Crear un ciclo for para ir subiendo todos los horarios
        for (schedule in schedules) {
            // Crea un objeto ScheduleUpload a partir del objeto Schedule
            val scheduleUpload = ScheduleUpload(schedule.horainicio, schedule.horafin, schedule
                .fecha, 1)
            
            var request = service.insertSchedule(scheduleUpload)
            
            request.enqueue(object : Callback<Schedule>{
                override fun onResponse(p0: Call<Schedule>, p1: Response<Schedule>) {
                    Log.d("Upload", "Schedule uploaded successfully: ${p1.body()}")
                }
                
                override fun onFailure(p0: Call<Schedule>, p1: Throwable) {
                    Log.e("Upload", "Failed to upload schedule")
                }
                
            })
        }
        
        deleteAllSchedules()
    }
}