package com.example.financetracker.view.dashboard_UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.financetracker.R
import com.example.financetracker.model.database.Transaction
import com.example.financetracker.model.database.UserDatabase
import com.example.financetracker.model.database.userDao
import com.example.financetracker.model.repositories.RoomAccessRepository
import com.example.financetracker.view.viewModelFactory.FinanceViewModelFactory
import com.example.financetracker.view.viewModelFactory.TransactionViewModelFactory
import com.example.financetracker.view.viewmodel.TransactionViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment() : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // should only contain the ui-related logic

        // this is only for the testing purpose of room(local database) -> i will use this in offline caching
        // when the button is clicked we need to store all the elements and then pass it to viewmodel
        val saveButton = view.findViewById<Button>(R.id.insertButton)
        saveButton.setOnClickListener {
            // check if button is clicking
            Log.d("button","clicked")
            insertData()
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_information, container, false)
    }

    // create a function to perform this operation
    private fun insertData(){
        // store the amount , note, source
        val amount = view?.findViewById<TextInputEditText>(R.id.amount)?.text.toString().toInt()
        val note = view?.findViewById<TextInputEditText>(R.id.note)?.text.toString()
        val source = view?.findViewById<TextInputEditText>(R.id.source)?.text.toString()
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

        // create object for the Transaction
        val transactionObject = Transaction(
            userId = userId,
            amount = amount,
            note = note,
            source = source,
        )
        Log.d("testing", "Transaction object created: $transactionObject")

        // now pass this object to viewModel
        // we require a viewModel provider
        val dao = UserDatabase.getInstance(requireContext()).userDao()
        val viewModel = ViewModelProvider(
            this,
            TransactionViewModelFactory(
                RoomAccessRepository(dao)
            )
        ).get(TransactionViewModel::class.java)

        // now we can access the viewmodel
        viewModel.insertData(transactionObject)
    }


}