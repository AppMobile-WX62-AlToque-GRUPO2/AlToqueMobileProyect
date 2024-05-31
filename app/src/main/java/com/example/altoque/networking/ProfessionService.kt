package com.example.altoque.networking

import com.example.altoque.models.Profession
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfessionService {
    @GET("professions/{id}")
    suspend fun getProfession(@Path("id") professionId: Int): Profession

    @PUT("professionsid}")
    suspend fun updateProfession(@Path("id") professionId: Int, @Body profession: Profession): Profession

    @GET("professions/{professionId}")
    fun getProfessionById(@Path("professionId") professionId: Int): Call<Profession>
}