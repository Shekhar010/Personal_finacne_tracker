package com.example.financetracker.model.repositories

import android.util.Log
import com.example.financetracker.model.database.IncomeSource
import com.example.financetracker.model.database.Transaction
import com.example.financetracker.model.database.User
import com.example.financetracker.model.database.userDao

class RoomAccessRepository (private val userDao : userDao) {
    // contain all the logic to perform operations in room
    // also exposes the functions for the viewmodel

    // function to insert the data in room
    suspend fun insertUser(user : User){
        // call the insertUser function
        userDao.insertUser(user)
    }

    // function to show all the income sources
    suspend fun insertIncomeSource(incomeSource: IncomeSource){
        userDao.addIncomeSource(incomeSource)
    }

    // function to insert the transaction record
    suspend fun addTransaction(transaction: Transaction){
        Log.d("tesing", "repository called and add transaction function also called from repo")
        userDao.addExpense(transaction)
    }

    // function to get the transaction from database
    suspend fun getFromTransaction() : List<Transaction>{
        Log.d("testing","repository called and get transaction function also called from repo")
        return userDao.getTransaction()
    }
}