package com.example.altoque.networking

import com.example.altoque.models.Notification
import retrofit2.Call
import retrofit2.http.GET

interface AlToqueService {
    @GET("/notifications")
    fun getNotifications(): Call<List<Notification>>

}