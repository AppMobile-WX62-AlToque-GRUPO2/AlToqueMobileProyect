package com.example.test2.networking

import com.example.test2.models.Specialist
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SpecialistService {
    @GET("specialists/{id}")
    fun getSpecialistById(
        @Path("id") id: Int
    ): Call<Specialist>
}
