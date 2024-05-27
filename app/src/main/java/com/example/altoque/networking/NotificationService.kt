package com.example.altoque.networking

import com.example.altoque.models.Notification
import com.example.altoque.models.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationService {
    @GET("notifications")
    fun getNotifications(): Call<List<Notification>>

    @GET("notifications")
    fun getNotificationsbyUser(@Query("userId") userId: Int): Call<List<Notification>>
}