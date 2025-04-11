package com.example.financetracker.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.model.database.Transaction
import com.example.financetracker.model.repositories.RoomAccessRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TransactionViewModel(val roomRepository: RoomAccessRepository) : ViewModel() {

    // this view model will take the data from the view and pass it to repository
    // this will have a function that will be used to insert the data

    fun insertData(transaction: Transaction) {
        Log.d("testing", "view model called")
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.addTransaction(transaction)
        }

        // after this get the data from the database and show it in log
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("testing" , "fetching....")
            val transaction = roomRepository.getFromTransaction()
            Log.d("testing", "${transaction.size}")
            for (t in transaction) {
                Log.d(
                    "testing, value fetched : ",
                    "${t.note} , ${t.amount}, ${t.source}, ${t.userId}, ${t.id}"
                )
            }
        }
    }

}

// object that will passed to the repository as the
data class TransactionObject(
    val userid: String,
    val amount: Int,
    val note: String,
    val source: String
)