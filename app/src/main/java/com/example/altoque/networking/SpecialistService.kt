package com.example.altoque.networking

import com.example.altoque.models.Specialist
import com.example.altoque.models.SpecialistIdResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface SpecialistService {
    @GET("specialists/{id}")
    suspend fun getSpecialist(@Path("id") specialistId: Int): Specialist

    @PUT("specialists/{id}")
    suspend fun updateSpecialist(@Path("id") specialistId: Int, @Body specialist: Specialist): Specialist

    @GET("specialists/{id}")
    fun getSpecialistById(@Path("id") id: Int): Call<Specialist>

    @GET("specialists/user/{user_id}/role/{role}")
    suspend fun getSpecialistIdByUserAndRole(
        @Path("user_id") userId: Int,
        @Path("role") role: String
    ): Response<SpecialistIdResponse>
}