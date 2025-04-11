package com.example.financetracker.view.dashboard_UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import com.example.financetracker.view.login_UI.Login_User
import com.example.financetracker.R
import com.example.financetracker.view.register_UI.Register_User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class Home : AppCompatActivity() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onStart() {
        // check if the user is logged in or not
        if (auth.currentUser == null) {
            val intent = Intent(this, Login_User::class.java)
            startActivity(intent)
            finish()
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // inflate the balance fragment in home activity
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView2, BalanceFragment())
            .commit()


        // sign out function for the user to sign out
        val signOutButton = findViewById<Button>(R.id.button2)
        signOutButton.setOnClickListener {
            // for testing purpose show the user email before sign out
            Log.d("current user : ", "email : ${auth.currentUser!!.email}")
            // sign out current user
            auth.signOut()

            // check if the user is logged in or not
            if (auth.currentUser == null) {
                // if the user is not logged in then go to the sign in activity
                val intent = Intent(this, Login_User::class.java)
                startActivity(intent)
                finish() // we use this finish so that when back is pressed then
                // the user will not be able to go back to the home activity
            } else {
                // for testing purpose show the user
                Log.d("current user : ", "email : ${auth.currentUser!!.email}")
            }
        }


        // using the intent we can go to the sign in activity
        val button = findViewById<Button>(R.id.button);

        button.setOnClickListener {
            // check and log the name of the user
            val intent = Intent(this, Register_User::class.java)
            startActivity(intent)
        }



        // for going to the profile page
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    // intent to go the home activity
                    supportFragmentManager.beginTransaction()
                        .replace(findViewById<FragmentContainerView>(R.id.fragmentContainerView2).id, BalanceFragment())
                        .commit()
                    true
                }
                R.id.nav_dashboard -> {
                    // intent to go the dashboard activity
                    supportFragmentManager.beginTransaction()
                        .replace(findViewById<FragmentContainerView>(R.id.fragmentContainerView2).id, DashboardFragment())
                        .commit()
                    true
                }
                R.id.nav_profile -> {
                    // we will inflate the profile fragment
                    supportFragmentManager.beginTransaction()
                        .replace(findViewById<FragmentContainerView>(R.id.fragmentContainerView2).id, ProfileFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }

    }
}