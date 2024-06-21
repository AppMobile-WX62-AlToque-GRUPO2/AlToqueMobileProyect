package com.example.altoque.networking

import com.example.altoque.models.Publication
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface PublicationService {
    @GET("/posts")
    fun getAllPublications(): Call<List<Publication>>

    @DELETE("/posts/{id}")
    fun deletePublication(@Path("id") id: Int): Call<Void>
}
