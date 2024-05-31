package com.example.altoque.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Publication (
    @PrimaryKey(autoGenerate = true)
    val id : Int?,

    @ColumnInfo
    val title : String,
    @ColumnInfo
    val description : String,
    @ColumnInfo
    val image : String,
    @ColumnInfo
    val address : String,
    @ColumnInfo
    val is_publish : Boolean,
    @ColumnInfo
    val clientID : Boolean,
)