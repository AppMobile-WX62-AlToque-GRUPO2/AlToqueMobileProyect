package com.example.altoque.networking

import com.example.altoque.models.Client
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClientService {
    @GET("clients/{id}")
    suspend fun getClient(@Path("id") clientId: Int): Client

    @PUT("clients/{id}")
    suspend fun updateClient(@Path("id") clientId: Int, @Body client: Client): Client

    @GET("clients/{id}")
    suspend fun getById(@Path("id") id: String): Response<Client>
}