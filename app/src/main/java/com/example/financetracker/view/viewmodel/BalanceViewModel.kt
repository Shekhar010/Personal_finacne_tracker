package com.example.financetracker.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.financetracker.model.repositories.FinanceRepository

class BalanceViewModel(private val repository1: FinanceRepository) : ViewModel()  {
    // i should not use this because it will create a new instance of the repository which will defeat the purpose of dependency injection
    // private val repository = FinanceRepository() // this will fetch the balance from the model layer

    // use the functions exposed by the repository
    val balance : LiveData<String?> = repository1.getBalance()



}



// data class that will hold the user data
// 1. name of the user
// 2. balance of the user
// 3. email of the user

data class UserData(
    val name : String,
    val balance : Double,
    val email : String
)

