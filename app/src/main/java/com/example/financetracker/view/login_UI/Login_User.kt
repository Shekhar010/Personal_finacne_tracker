package com.example.financetracker.view.login_UI

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.financetracker.R
import com.example.financetracker.view.dashboard_UI.Home
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Login_User : AppCompatActivity() {
    // firebase instance for both -> google authentication purpose -> email password login
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // change the background color of the login page



        // initialize the firebase auth instance
        this.auth = FirebaseAuth.getInstance()

        // 1. first way of authentication
        // user can authenticate using the email and password
        val signInButton = findViewById<Button>(R.id.credentialAuthButton)
        // set the onclick listener for the authentication (start authentication of the user using email and pass)
        signInButton.setOnClickListener {
            // progress bar should be visible
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            // sign in button and forgotten password should be invisible
            signInButton.visibility = View.INVISIBLE
            findViewById<TextView>(R.id.textView2).visibility = View.INVISIBLE

            // store the email and password in the variables after the user attempt to sign in
            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            // 1. call the function to authenticate the user
            loginUser(email, password)
        }


        // second way of authentication
        // user can authenticate using the google
        val googleAuthButton = findViewById<Button>(R.id.googleAuthButton)


        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))  // Get from `google-services.json`
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // set the click listener
        googleAuthButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    // function to login using the the firebase authentication using the email and password
    private fun loginUser(email: String, password: String) {
        this.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = this.auth.currentUser
                Log.d("current user ${user?.email}", "logged in successfully")

                // make the color the strip green
                findViewById<TextView>(R.id.textView3).visibility = View.VISIBLE
                findViewById<TextView>(R.id.textView3).setBackgroundColor(Color.GREEN)
                // launch a coroutine
                val strip = lifecycleScope.async() {
                    delay(2000)
                    findViewById<TextView>(R.id.textView3).visibility = View.INVISIBLE
                }
                // destroy the coroutine
                strip.cancel()

                // launch the home activity
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish() // so that on back pressed the user will not be able to go back to the sign in activity
            } else {
                Log.d("login failed", "login failed")
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE
                findViewById<Button>(R.id.credentialAuthButton).visibility = View.VISIBLE
                findViewById<TextView>(R.id.textView2).visibility = View.VISIBLE

                // change the color of the strip to red
                findViewById<TextView>(R.id.textView3).visibility = View.VISIBLE
                findViewById<TextView>(R.id.textView3).setBackgroundColor(Color.RED)
                // launch a coroutine
                lifecycleScope.launch {
                    delay(2000)
                    findViewById<TextView>(R.id.textView3).visibility = View.INVISIBLE

                }


            }
        }
    }

    // Function to start Google Sign-In
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("SignIn", "firebaseAuthWithGoogle: ${account.id}")
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("SignIn", "Google sign in failed", e)
            }
        }

    // Authenticate with Firebase
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        this.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("SignIn", "signInWithCredential:success")
                    val user = this.auth.currentUser
                    Log.d("User", "User: ${user?.displayName}, Email: ${user?.email}")
                } else {
                    Log.w("SignIn", "signInWithCredential:failure", task.exception)
                }
            }
    }

}