package com.example.altoque.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.altoque.models.Schedule

@Dao
interface ScheduleDao {
    // CRUD
    @Query("SELECT * FROM Schedule")
    fun getAll() : List<Schedule>
    
    @Insert
    fun insertSchedule(vararg schedule: Schedule)
    
    @Update
    fun updateSchedule(vararg schedule: Schedule)
    
    @Delete
    fun deleteSchedule(vararg schedule: Schedule)
    
    @Query("DELETE FROM Schedule")
    fun deleteAll()
}