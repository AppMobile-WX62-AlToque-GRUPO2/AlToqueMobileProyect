package com.example.test2.networking

import com.example.test2.models.AvailableDate
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AvailableDateService {
    @GET("availableDates/{id}")
    fun getAvailableDateById(
        @Path("id") id: Int
    ): Call<AvailableDate>
}
