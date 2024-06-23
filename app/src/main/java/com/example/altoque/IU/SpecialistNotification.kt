package com.example.altoque.IU

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.altoque.R
import com.example.altoque.adapter.NotificationAdapter
import com.example.altoque.models.Notification
import com.example.altoque.networking.ClientService
import com.example.altoque.networking.NotificationService
import com.example.altoque.networking.SharedPreferences
import com.example.altoque.networking.UserService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpecialistNotification : AppCompatActivity() {

    lateinit var notifications : List<Notification>
    lateinit var notificationAdapter: NotificationAdapter
    var userId: Int = 0

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
        val sharedPreference = SharedPreferences(this)
        val datosUser = sharedPreference.getData("UserData")
        if (datosUser != null) {
            userId = datosUser.id
            loadNotification(userId)
        } else {
            Toast.makeText(this, "No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loadNotification(userId: Int) {

            val rvSpecialistNoti = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvSpecialistNotification)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://altoquebackendapi.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(NotificationService::class.java)

            val request = service.getNotificationsbyUser(userId)

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

        val clientService = retrofit.create(ClientService::class.java)
        val userService = retrofit.create(UserService::class.java)

        lifecycleScope.launch {
            try {
                val clientResponse = clientService.getClient(1)

                val userId = clientResponse.userId
                val userResponse = userService.getUser(userId)

                runOnUiThread {
                    val rvNotiImage = findViewById<ImageView>(R.id.rvNotiImage)
                    if (userResponse.avatar != null) {
                        Glide.with(this@SpecialistNotification)
                            .load(userResponse.avatar)
                            .placeholder(R.drawable.default_user)
                            .into(rvNotiImage)
                    } else {
                        //Si no hay imagen se le da una imagen default
                        rvNotiImage.setImageResource(R.drawable.default_user)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@SpecialistNotification, "Error al cargar la información", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}