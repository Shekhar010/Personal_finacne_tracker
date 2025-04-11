package com.example.financetracker.view.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.financetracker.model.repositories.RoomAccessRepository
import com.example.financetracker.view.viewmodel.BalanceViewModel
import com.example.financetracker.view.viewmodel.TransactionViewModel

class TransactionViewModelFactory(private val repository: RoomAccessRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}