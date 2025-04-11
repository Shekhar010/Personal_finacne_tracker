package com.example.financetracker.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// this class will be used to create the tables in the database
@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    val Name : String,
    val Email : Int
)

// we can create multiple tables
@Entity(tableName = "income_source_table")
data class IncomeSource(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    val userId : String,
    val sourceName : String,
    val amount : Int
)

// we can store the transaction of the user
@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    val userId : String,
    val amount : Int,
    val note : String,
    val source : String
)
