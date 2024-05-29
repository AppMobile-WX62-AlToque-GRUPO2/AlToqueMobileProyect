package com.example.altoque.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contract (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo
    var state: Int?,
    @ColumnInfo
    val specialistId: Int?,
    @ColumnInfo
    val availableDateId: Int?
)