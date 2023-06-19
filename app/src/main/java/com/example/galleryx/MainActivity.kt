package com.example.galleryx

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    //Declartion
    private lateinit var Join_Button:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Join_Button = findViewById(R.id.btn_join)

        Join_Button.setOnClickListener()
        {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
}