package com.example.test2.database

import androidx.room.Dao
import androidx.room.Query
import com.example.test2.models.Publication

@Dao
interface PublicationDao {

    @Query("SELECT * FROM Publication")
    fun getAllPublications() : List<Publication>
    @Query("DELETE FROM Publication WHERE id = :id")
    fun deletePublication(id : Int)
}