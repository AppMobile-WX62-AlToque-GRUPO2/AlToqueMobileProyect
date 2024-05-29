package com.example.test2.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

class User (
        val id: Int,
        val avatar: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val phone: String,
        val description : String
)