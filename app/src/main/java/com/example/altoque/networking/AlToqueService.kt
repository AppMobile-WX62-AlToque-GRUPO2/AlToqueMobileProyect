package com.example.altoque.networking

import com.example.altoque.models.Notification
import com.example.altoque.models.Post
import retrofit2.Call
import retrofit2.http.GET

interface AlToqueService {
    @GET("/notifications")
    fun getNotifications(): Call<List<Notification>>

    @GET("/posts")
    fun getPosts(): Call<List<Post>>

    @GET("?")
    fun getPostsbyClient(): Call<List<Post>>

}