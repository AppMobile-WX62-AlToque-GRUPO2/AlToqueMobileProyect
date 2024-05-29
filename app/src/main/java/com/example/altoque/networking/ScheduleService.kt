package com.example.altoque.networking

import com.example.altoque.models.Schedule
import com.example.altoque.models.ScheduleUpload
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ScheduleService {
    @POST("availableDates")
    fun insertSchedule(@Body schedule: ScheduleUpload) : Call<Schedule>
}