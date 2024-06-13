package com.example.altoque.networking

import com.example.altoque.models.Login
import com.example.altoque.models.Register
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AltoqueService {

    @POST("/auth/login")
    fun postLogin(@Body login: Login): Call<Login>

    @POST("/auth/register")
    fun postRegister(@Body register: Register): Call<Register>
}