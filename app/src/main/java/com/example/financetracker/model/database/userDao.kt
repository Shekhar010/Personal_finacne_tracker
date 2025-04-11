package com.example.financetracker.model.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface userDao {
    // it will contain all the query you want to add

    // QUERIES
    // 1. query to insert data to the table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert
    suspend fun addIncomeSource(incomeSource: IncomeSource)

//    // 2. query to see all the incomeSources
//    @Query("SELECT * FROM income_source_table WHERE userId = :userId")
//    suspend fun getAllIncomeSources(userId: String): LiveData<List<IncomeSource>>

    // 3. query to insert the transaction done by the user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExpense(transaction: Transaction)

    // 4. query to get the data from the Transaction table
    @Query("Select * from transaction_table")
    suspend fun getTransaction() : List<Transaction>




}