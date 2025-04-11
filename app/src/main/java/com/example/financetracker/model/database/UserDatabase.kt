package com.example.financetracker.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class, IncomeSource::class, Transaction::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    // define a method to return dao
    abstract fun userDao() : userDao
    
    companion object {
        @Volatile
        private var INSTANCE : UserDatabase? = null

        // create function to return the instance of the database
        fun getInstance(context : Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "finance_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }


}