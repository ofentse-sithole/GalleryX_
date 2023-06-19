package com.example.galleryx

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class Goal : AppCompatActivity() {
    private lateinit var editGoal: EditText
    private lateinit var btnGoal: Button
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)

        editGoal = findViewById(R.id.edit_Goal_achieved)
        btnGoal = findViewById(R.id.btnGoal)

        btnGoal.setOnClickListener {
            // Set the sneaker goal
            val goal = editGoal.text.toString()
            if (goal.isNotEmpty()) {
                // do something with goal
            }
            finish()

        }



        // Initialize and set the bottom navigation view
        bottomNavView = findViewById(R.id.bottomNavigationView)

        // Set the bottom navigation view to be selected when the user is on the homepage
        bottomNavView.selectedItemId = R.id.menu_goal

        // Set the onNavigationItemSelectedListener for the bottom navigation view
        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // Do nothing because the user is already on the homepage
                    startActivity(Intent(this, ArtCollections::class.java))
                    Toast.makeText(this,"Art Collection selected", Toast.LENGTH_SHORT).show()
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
                    menuItem.isChecked=true
                    true
                }

                R.id.menu_achievements -> {
                    startActivity(Intent(this, Achievements::class.java))
                    Toast.makeText(this,"Achievements selected", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.menu_profile -> {
                    // Start the profile activity
                    startActivity(Intent(this, Profile::class.java))
                    Toast.makeText(this,"Profile selected", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }
}