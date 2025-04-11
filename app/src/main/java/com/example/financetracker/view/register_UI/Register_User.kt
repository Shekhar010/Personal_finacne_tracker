package com.example.financetracker.view.register_UI

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.financetracker.R
import com.example.financetracker.view.login_UI.Login_User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register_User : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // for the confirm password field we can check if the password matches or not in another thread
        val t1 = Thread {
            while (true) {
                runOnUiThread {
                    val password =
                        findViewById<TextInputEditText>(R.id.textboxPassword).text?.toString()
                    val confirmPassword =
                        findViewById<TextInputEditText>(R.id.textboxConfirmPassword).text?.toString()

                    val passwordField1 = findViewById<TextInputLayout>(R.id.textInputLayout5)
                    val passwordField2 = findViewById<TextInputLayout>(R.id.textInputLayout4)

                    // only when the password is not empty in both the fields and the confirm password field is being filled
                    if (password != "" && confirmPassword != "") {
                        val color = if (password == confirmPassword) Color.GREEN else Color.RED

                        // Set persistent color for the outline
                        passwordField1.setBoxStrokeColorStateList(ColorStateList.valueOf(color))
                        passwordField2.setBoxStrokeColorStateList(ColorStateList.valueOf(color))
                    }
                }
                Thread.sleep(500)  // Reduce CPU usage
            }
        }
        t1.start()


        // initialize the firestore
        val db = FirebaseFirestore.getInstance()


        // 1. now we can use the firebase createUserWithEmailAndPassword function to create a new user
        // button to start the registration process
        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            // store the name, email, password of the user
            val name = findViewById<TextInputEditText>(R.id.textboxName).text.toString()
            val email = findViewById<TextInputEditText>(R.id.textboxEmail).text.toString()
            val password = findViewById<TextInputEditText>(R.id.textboxPassword).text.toString()
            val confirmPassword =
                findViewById<TextInputEditText>(R.id.textboxConfirmPassword).text.toString()

            // check password and confirm password fields match and are not empty
            if (password == confirmPassword && password != "") {
                // start the registration process
                // create the instance of the firebase authentication
                val auth = FirebaseAuth.getInstance()

                // now create the new user
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        // if the registration is successfully done
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid

                            // data of the users
                            val user = hashMapOf(
                                "name" to name,
                                "email" to email,
                                "password" to password
                            )

                            // when the user is successfully registered in the database
                            if (userId != null) {
                                db.collection("users").document(userId)
                                    .set(user)
                                    .addOnCompleteListener { documentReference ->
                                        Log.d(
                                            TAG,
                                            "DocumentSnapshot added with ID: ${documentReference.result}"
                                        )
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("Firestore", "Error adding user", e)
                                    }
                            }

                            // log it for testing purpose
                            Log.d(TAG, "createUserWithEmail:success")
                            // now go to the login activity
                            val intent = Intent(this, Login_User::class.java)
                            startActivity(intent)
                            finish() // so that user doesn't come back
                        }
                        // else
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }
    }
}