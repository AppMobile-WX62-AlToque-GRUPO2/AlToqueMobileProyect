package com.example.test2.networking

import com.example.test2.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users/{id}")
    fun getUserById(
        @Path("id") id: Int
    ): Call<User>
}
