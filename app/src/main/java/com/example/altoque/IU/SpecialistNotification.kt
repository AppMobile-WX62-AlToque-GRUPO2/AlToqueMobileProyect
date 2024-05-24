package com.example.altoque.IU

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.example.altoque.R
import com.example.altoque.adapter.NotificationAdapter
import com.example.altoque.models.Notification
import com.example.altoque.networking.AlToqueService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpecialistNotification : AppCompatActivity() {

    lateinit var notifications : List<Notification>
    lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_specialist_notification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constrain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        notificationAdapter = NotificationAdapter(emptyList())
        loadNotification()

    }

    private fun loadNotification() {

            val rvSpecialistNoti = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvSpecialistNotification)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://altoquebackendapi.onrender.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(AlToqueService::class.java)

            val request = service.getNotifications()

            request.enqueue(object : retrofit2.Callback<List<Notification>> {
                override fun onResponse(call: Call<List<Notification>>, response: retrofit2.Response<List<Notification>>) {
                    if (response.isSuccessful) {
                        notifications = response.body()!!
                        notificationAdapter = NotificationAdapter(notifications)
                        rvSpecialistNoti.adapter = notificationAdapter
                        rvSpecialistNoti.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@SpecialistNotification)
                    }
                }

                override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                    println("Error")
                }
            })
    }

}