package com.example.galleryx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.loginUsername)
        password = findViewById(R.id.loginPassword)
        loginButton = findViewById(R.id.btn_login)
        registerButton = findViewById(R.id.btn_register)

        // Initialize Firebase authentication and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        registerButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            Toast.makeText(this, "Register button clicked", Toast.LENGTH_LONG).show()
        }

        loginButton.setOnClickListener {
            val usernameText = username.text.toString().trim()
            val passwordText = password.text.toString().trim()

            if (!TextUtils.isEmpty(usernameText) && !TextUtils.isEmpty(passwordText)) {
                db.collection("users").whereEqualTo("username", usernameText).get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val userData = documents.documents[0].toObject(User::class.java)
                            if (userData != null && userData.password == passwordText) {
                                auth.signInWithEmailAndPassword(usernameText, passwordText)
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            val message = "Login successful: Username=$usernameText"
                                            val intent = Intent(this, ArtCollections::class.java)
                                            startActivity(intent)
                                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                        } else {
                                            val message = "Login failed: ${task.exception?.message}"
                                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                val message = "Incorrect username or password"
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val message = "User not found"
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        val message = "Error: ${exception.message}"
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
