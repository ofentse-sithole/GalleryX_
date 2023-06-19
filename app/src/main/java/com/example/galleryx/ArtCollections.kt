package com.example.galleryx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.galleryx.databinding.ActivityArtCollectionsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ArtCollections : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var AddCollections:Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art_collections)

               AddCollections = findViewById(R.id.btn_add_collections)

        AddCollections.setOnClickListener()
        {
            val intent = Intent(this, ArtRooms::class.java)
            startActivity(intent)
            Toast.makeText(this,"Adding Collections",Toast.LENGTH_LONG).show()
        }



        // Initialize and set the bottom navigation view
        bottomNavView = findViewById(R.id.bottomNavigationView)

        // Set the bottom navigation view to be selected when the user is on the homepage
        bottomNavView.selectedItemId = R.id.menu_home

        // Set the onNavigationItemSelectedListener for the bottom navigation view
        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // Do nothing because the user is already on the homepage
                    menuItem.isChecked=true
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
                    startActivity(Intent(this, Profile::class.java))
                    Toast.makeText(this,"Profile selected", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}