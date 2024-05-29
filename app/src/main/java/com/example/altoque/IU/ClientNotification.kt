package com.example.altoque.IU

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.altoque.R
import com.example.altoque.adapter.NotificationAdapter
import com.example.altoque.models.Notification
import com.example.altoque.networking.NotificationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ClientNotification : AppCompatActivity()  {

    lateinit var notifications : List<Notification>
    lateinit var notificationAdapter: NotificationAdapter
    var userId: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_notification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constrain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        notificationAdapter = NotificationAdapter(emptyList())
        userId = intent.getIntExtra("userId", 1)
        loadNotification(userId)

    }

    private fun loadNotification(userId: Int) {

        val rvClientNoti = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvClientNoti)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://altoquebackendapi.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(NotificationService::class.java)

        val request = service.getNotificationsbyUser(userId)

        request.enqueue(object : retrofit2.Callback<List<Notification>> {
            override fun onResponse(call: retrofit2.Call<List<Notification>>, response: retrofit2.Response<List<Notification>>) {
                if (response.isSuccessful) {
                    notifications = response.body()!!
                    notificationAdapter = NotificationAdapter(notifications)
                    rvClientNoti.adapter = notificationAdapter
                    rvClientNoti.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@ClientNotification)
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Notification>>, t: Throwable) {
                println("Error")
            }
        })
    }
}