package com.example.altoque.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Schedule (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    
    @ColumnInfo
    val fecha : String,
    
    @ColumnInfo
    val horainicio : String,
    
    @ColumnInfo
    val horafin : String
)

class ScheduleUpload (
    @SerializedName("start_time")
    val horainicio : String,
    
    @SerializedName("end_time")
    val horafin : String,
    
    @SerializedName("day")
    val fecha : String,
    
    @SerializedName("postId")
    val post_id : Int
)