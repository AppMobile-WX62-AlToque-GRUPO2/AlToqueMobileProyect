package com.example.altoque.models

data class User(
    val id: Int,
    val password: String,
    val email: String,
    val role: Boolean,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val birthdate: String,
    val avatar: String?,
    val rating: Int?,
    val description: String?,
    val ubicationId: Int
)