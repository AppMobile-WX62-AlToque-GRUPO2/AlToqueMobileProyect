package com.example.altoque.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo
    val address: String,
    @ColumnInfo
    val is_publish: Boolean,
    @ColumnInfo
    val image: String,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val description: String,
    @ColumnInfo
    val clientId: String,
)