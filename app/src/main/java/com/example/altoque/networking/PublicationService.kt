package com.example.altoque.networking

import com.example.altoque.models.Publication
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET

interface PublicationService {
    @GET("/posts")
    fun getAllPublications() : Call<List<Publication>>
    @DELETE("/posts/{id}")
    fun deletePublication() : Call<Void>
}