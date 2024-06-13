package com.example.altoque.networking

import com.example.altoque.models.Login
import com.example.altoque.models.Register
import com.example.altoque.models.TokenLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AltoqueService {

    @POST("/auth/login")
    fun postLogin(@Body login: Login): Call<Login>

    @POST("/auth/register")
    fun postRegister(@Body register: Register): Call<Register>

    @GET("/auth/token_info")
    fun getTokenInfo(): Call<TokenLogin>

}