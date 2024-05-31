package com.example.altoque.networking

import com.example.altoque.models.AvailableDate
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AvailableDateService {
    @GET("availableDates/{id}")
    fun getAvailableDateById(
        @Path("id") id: Int
    ): Call<AvailableDate>
}