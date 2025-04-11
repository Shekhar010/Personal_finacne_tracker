package com.example.financetracker.model.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FinanceRepository() {
    // this class will contain the login to fetch the details such as :
    // 1. balance
    // 2. user-name
    // 3. transaction and other financial details

    // instance of firestore
    val db = FirebaseFirestore.getInstance()

    // user id
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // reference to the user document
    val userDocumentRef = db.collection("users").document(userId!!)

    // livedata for balance
    var balanceLiveData = MutableLiveData<String?>()

    // to fetch the balance from firestore we need to expose the function for the viewModel
    fun getBalance(): LiveData<String?> {
        // this will return the balance of the user
        userDocumentRef.get()
            .addOnSuccessListener { document ->
                // fetch the balance and store it in currentBalance
                balanceLiveData.postValue(document.get("balance").toString())
            }
            .addOnFailureListener { Exception ->
                balanceLiveData.postValue(null)
            }
        return balanceLiveData
    }

    // function to update the balance
    fun updateBalance(newBalance: String) {
        userDocumentRef.update("balance", newBalance)

        // after updating the balance we need to update the balanceLiveData
        getBalance()
    }


}