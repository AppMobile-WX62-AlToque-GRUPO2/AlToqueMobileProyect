package com.example.altoque.networking

import com.example.altoque.models.Post
import com.example.altoque.models.PostResponse
import com.example.altoque.models.PostUpload
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
    
    @GET("posts")
    fun getAllPosts(): Call<List<PostResponse>>
    
    @GET("posts/{post_id}")
    fun getPostById(@Path("post_id") postId: Int): Call<PostResponse>
    
    @POST("posts")
    fun insertPost(@Body post: PostUpload) : Call<PostResponse>
}