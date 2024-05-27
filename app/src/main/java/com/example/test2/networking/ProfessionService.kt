package com.example.test2.networking

import com.example.test2.models.Profession
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call

interface ProfessionService {
    @GET("professions/{professionId}")
    fun getProfessionById(@Path("professionId") professionId: Int): Call<Profession>
}