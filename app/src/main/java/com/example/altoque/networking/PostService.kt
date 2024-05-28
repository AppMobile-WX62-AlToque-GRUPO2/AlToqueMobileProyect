package com.example.altoque.networking

import com.example.altoque.models.Post
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostService {

    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("posts")
    fun getPostsbyClient(@Query("clientId") clientId: Int): Call<List<Post>>

    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: String): Response<Post>
}