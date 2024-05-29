package com.example.altoque.networking

import com.example.altoque.models.UpdateUserRequest
import com.example.altoque.models.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): User

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") userId: Int, @Body user: UpdateUserRequest): User
}