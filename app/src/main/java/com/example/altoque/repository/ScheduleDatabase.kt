package com.example.altoque.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.altoque.models.Schedule

@Database(entities = arrayOf(Schedule::class), version = 1)
abstract class ScheduleDatabase : RoomDatabase() {
    // Enlazamos con el DAO de la bd
    abstract fun getDao() : ScheduleDao
    
    // Utilizamos el patrón singleton
    companion object {
        // Creamos una INSTANCIA nula
        private var INSTANCE : ScheduleDatabase? = null
        // Creamos fun para obtener la base de datos unica
        fun getInstance(context: Context) : ScheduleDatabase{
            if (INSTANCE == null){
                INSTANCE = Room
                    .databaseBuilder(context, ScheduleDatabase::class.java, "schedulev4.db")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE as ScheduleDatabase
        }
    }
}