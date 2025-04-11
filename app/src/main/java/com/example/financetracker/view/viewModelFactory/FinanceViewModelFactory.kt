package com.example.financetracker.view.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.financetracker.model.repositories.FinanceRepository
import com.example.financetracker.view.viewmodel.BalanceViewModel

class FinanceViewModelFactory(private val repository: FinanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BalanceViewModel::class.java)) {
            return BalanceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

