package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.altoque.R
import com.example.altoque.models.Schedule
import com.example.altoque.repository.ScheduleDatabase

class ScheduleActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_schedule)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        var btAcceptSchedule = findViewById<Button>(R.id.btAcceptSchedule)
        btAcceptSchedule.setOnClickListener {
            addNewSchedule()
            val intent = Intent(this, ScheduleListActivity::class.java)
            startActivity(intent)
        }
        
        var btCancelSchedule = findViewById<Button>(R.id.btCancelSchedule)
        btCancelSchedule.setOnClickListener {
            val intent = Intent(this, ScheduleListActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun addNewSchedule() {
        var etDatePostCreation = findViewById<EditText>(R.id.etDatePostCreation)
        var etStartTimePostCreation = findViewById<EditText>(R.id.etStartTimePostCreation)
        var etEndTimePostCreation = findViewById<EditText>(R.id.etEndTimePostCreation)
        
        // Transformar de tipo Editable a String
        var date = etDatePostCreation.text.toString()
        var startTime = etStartTimePostCreation.text.toString()
        var endTime = etEndTimePostCreation.text.toString()
        
        // TODO: Validar que los datos no esten vacios o que los datos sean erroneos
        
        // Agregar el horario a la BD
        ScheduleDatabase
            .getInstance(this)
            .getDao()
            .insertSchedule(Schedule(null, date, startTime, endTime))
    }
}