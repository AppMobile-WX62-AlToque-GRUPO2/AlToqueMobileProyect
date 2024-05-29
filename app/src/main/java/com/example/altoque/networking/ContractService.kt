package com.example.altoque.networking

import com.example.altoque.models.Contract
import retrofit2.Response
import retrofit2.http.GET

interface ContractService {

    @GET("posts")//CAMBIAR A contracts :V
    suspend fun getAll(): Response<List<Contract>>
}