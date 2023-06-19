package com.example.galleryx

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Profile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var tvUsername: TextView
    private lateinit var etCurrentPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var btnChangePassword: Button
    private lateinit var etDeleteAccountPassword: EditText
    private lateinit var btnDeleteAccount: Button
    private lateinit var btnLogout: Button
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        tvUsername = findViewById(R.id.DeleteUsername)
        etCurrentPassword = findViewById(R.id.ProfileCurrentPassword)
        etNewPassword = findViewById(R.id.ProfileNewPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        etDeleteAccountPassword = findViewById(R.id.DeleteAccountPassword)
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount)
        btnLogout = findViewById(R.id.btnLogout)


        // Fetch and display user information
        val currentUser = auth.currentUser
        currentUser?.let {
            val username = currentUser.displayName
            tvUsername.text = "Username: $username"
        }

        btnChangePassword.setOnClickListener {
            val currentPassword = etCurrentPassword.text.toString().trim()
            val newPassword = etNewPassword.text.toString().trim()

            // Perform change password logic using Firebase Auth
            val user = auth.currentUser
            if (user != null && currentPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
                user.reauthenticate(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPassword).addOnCompleteListener {task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this, "Error updating password", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_LONG).show()
            }

            etCurrentPassword.text.clear()
            etNewPassword.text.clear()
        }

        btnDeleteAccount.setOnClickListener {
            val deleteAccountPassword = etDeleteAccountPassword.text.toString().trim()

            // Perform delete account logic using Firebase Auth
            val user = auth.currentUser
            if (user != null && deleteAccountPassword.isNotEmpty()) {
                val credential = EmailAuthProvider.getCredential(user.email!!, deleteAccountPassword)
                user.reauthenticate(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.delete().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this, Login::class.java))
                            } else {
                                Toast.makeText(this, "Failed to delete account", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter the account password", Toast.LENGTH_LONG).show()
            }

            etDeleteAccountPassword.text.clear()
        }

        btnLogout.setOnClickListener {
            // Perform logout logic using Firebase Auth
            auth.signOut()

            // Notify the user and start the login activity
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, Login::class.java))
        }

        // Initialize and set the bottom navigation view
        bottomNavView = findViewById(R.id.bottomNavigationView)

        // Set the bottom navigation view to be selected when the user is on the homepage
        bottomNavView.selectedItemId = R.id.menu_profile

        // Set the onNavigationItemSelectedListener for the bottom navigation view
        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // Do nothing because the user is already on the homepage
                    startActivity(Intent(this, ArtCollections::class.java))
                    Toast.makeText(this,"Art Collections selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_category -> {
                    // Start the search activity
                    startActivity(Intent(this, ArtRooms::class.java))
                    Toast.makeText(this,"Category selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_goal -> {
                    // Start the goal activity
                    startActivity(Intent(this, Goal::class.java))
                    Toast.makeText(this,"Goal selected", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.menu_achievements -> {
                    startActivity(Intent(this, Achievements::class.java))
                    Toast.makeText(this,"Achievements selected", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.menu_profile -> {
                    // Start the profile activity
                    menuItem.isChecked=true
                    true
                }
                else -> false
            }

        }
    }
}