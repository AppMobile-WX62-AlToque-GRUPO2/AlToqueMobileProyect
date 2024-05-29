package com.example.altoque.networking

import com.example.altoque.models.Post
import com.example.altoque.models.PostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostService {
    
    @GET("posts")
    fun getAllPosts(): Call<List<PostResponse>>
    
    @GET("posts/{post_id}")
    fun getPostById(@Path("post_id") postId: Int): Call<PostResponse>
    
    @POST("posts")
    fun insertPost(@Body post: Post) : Call<PostResponse>
}