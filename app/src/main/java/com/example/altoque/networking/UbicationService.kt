package com.example.altoque.networking

import com.example.altoque.models.Ubication
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UbicationService {
    @GET("ubications/{id}")
    suspend fun getUbication(@Path("id") ubicationId: Int): Ubication

    @PUT("ubications/{id}")
    suspend fun updateUbication(@Path("id") ubicationId: Int, @Body ubication: Ubication): Ubication
}