package com.example.altoque.networking

import com.example.altoque.models.Contract
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ContractService {

    @GET("posts")//CAMBIAR A contracts :V
    suspend fun getAll(): Response<List<Contract>>

    @GET("contracts")
    fun getContractsByState(@Query("state") state: Int): Call<List<Contract>>
}