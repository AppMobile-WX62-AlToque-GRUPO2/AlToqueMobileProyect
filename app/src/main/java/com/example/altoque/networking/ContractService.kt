package com.example.altoque.networking

import com.example.altoque.models.Contract
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ContractService {

    @GET("contracts")
    suspend fun getAll(): Response<List<Contract>>

    @GET("contracts")
    fun getContractsByState(@Query("state") state: Int): Call<List<Contract>>

    @PUT("contracts/{id}")
    suspend fun updateContract(@Path("id") id: Int, @Body contract: Contract): Response<Contract>
  
    @PUT("contracts/{id}")
    fun updateContractState(@Path("id") id: Int, @Body contract: Contract): Call<Contract>