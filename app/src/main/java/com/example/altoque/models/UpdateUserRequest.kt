package com.example.altoque.models

class UpdateUserRequest (
    val email: String,
    val role: Boolean,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val birthdate: String,
    val avatar: String?,
    val description: String?,
    val ubicationId: Int
)