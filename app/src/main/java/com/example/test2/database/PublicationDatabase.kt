package com.example.test2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.test2.models.Publication

@Database(entities = arrayOf(Publication::class), version = 1)
abstract class PublicationDatabase : RoomDatabase() {

    abstract fun getPublicationDao() : PublicationDao

    companion object {
        private var INSTANCE : PublicationDatabase? = null

        fun getInstance(context: Context) : PublicationDatabase{
            if (INSTANCE == null){
                INSTANCE = Room
                    .databaseBuilder(context, PublicationDatabase::class.java, "altoquedb.db")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE as PublicationDatabase
        }
    }

}