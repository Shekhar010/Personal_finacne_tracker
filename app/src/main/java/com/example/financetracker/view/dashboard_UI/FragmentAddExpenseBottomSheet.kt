package com.example.financetracker.view.dashboard_UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.financetracker.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FragmentAddExpenseBottomSheet : BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.activity_fragment_add_expense_bottom_sheet,
            container,
            false
        )
    }

    data class transaction(
        val amount: String,
        val source: String,
        val note: String
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // for the amount user enter in the expenditure
        val enteredAmount = view.findViewById<EditText>(R.id.expenditureAmount)
        // for the note
        val userNote = view.findViewById<EditText>(R.id.editText)
        // for source
        val sourceSwitch = view.findViewById<SwitchMaterial>(R.id.switch1)

        // initialize firestore
        val db = FirebaseFirestore.getInstance()

        // get the user id
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // reference to the users document
        val userRef = db.collection("users").document(userId)


        // when the button is clicked then we have to store
        // 1. entered amount
        // 2. note if added
        // 3. from where the amount is used i. from current balance ii. or from the saving
        // 4. take an extra confirmation


        val addExpenseButton = view.findViewById<Button>(R.id.buttonForExpense)
        addExpenseButton.setOnClickListener {
            // get the text
            val amount = enteredAmount.text
            Log.d("amount", "amount: $amount")

            // now get the note if user added a note
            val note = userNote.text
            Log.d("note", "note: $note")

            // now get the source from the user
            val source = if (sourceSwitch.isChecked) "Saving" else "Current Balance"
            Log.d("source", "source: $source")

            // store the information in hashmap for
            val transaction = transaction(
                amount.toString(),
                source,
                note.toString()
            )

            // these logs are for testing purpose

            // now store the above data to firestore
            // first check if the array for transaction exist in the user document
            userRef.get().addOnSuccessListener { document ->
                // if transaction array exist
                if (document.exists() && document.contains("transactions")) {
                    Log.d("transaction array", "transaction array exist")
                    // add the new transaction to the array
                    // using try catch to avoid run time error
                    try {
                        userRef.update("transactions", FieldValue.arrayUnion(transaction))
                            .addOnSuccessListener {
                                // for testing purpose
                                Log.d("transaction", "saved details successfully")
                            }
                    } catch (exception: Exception) {
                        Log.d("error", "error while saving the details $exception")
                    }


                } else { // if document doesn't exist
                    Log.d("error", "the array no such array or the user document exist")
                }
            }
            // also now deduct the balance from the current balance or saving which ever was used to add the expense


            // and close the bottomSheet
            dismiss()
        }

        // close the bottom sheet
        val closeButton = view.findViewById<ImageView>(R.id.imageView)
        closeButton.setOnClickListener {
            dismiss()
        }

    }


}
