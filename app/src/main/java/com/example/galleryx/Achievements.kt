package com.example.galleryx

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

private lateinit var progressBar: ProgressBar
private lateinit var percentageTextView: TextView
private val achievements: MutableMap<String, Boolean> = mutableMapOf(
    "Starter" to false,
    "Collector" to false,
    "Packrat" to false
)
private var itemCount = 0
private lateinit var bottomNavView: BottomNavigationView
private lateinit var itemCountTextView:TextView
private lateinit var tvAchievementCollector:TextView
private lateinit var tvAchievementStarter:TextView
private lateinit var tvAchievementPackrat:TextView


class Achievements : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private val db = FirebaseFirestore.getInstance()
    private val collectionPath = "Art"
    private val achievementStarter = 1
    private val achievementCollector = 3
    private val achievementPackrat = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        tvAchievementStarter = findViewById(R.id.tvAchievementStarter)
        tvAchievementCollector = findViewById(R.id.tvAchievementCollector)
        tvAchievementPackrat = findViewById(R.id.tvAchievementPackrat)
        itemCountTextView = findViewById(R.id.txtAchievements)


        db.collection(collectionPath).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e(TAG, "Error fetching art collection", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val artCount = snapshot.size()
                updateAchievement(artCount)
            }
        }


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
                    startActivity(Intent(this,Goal::class.java))
                    Toast.makeText(this,"Goal selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_achievements -> {
                    // Already on the achievements page
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

        // Initialize Firestore
        //firestore = FirebaseFirestore.getInstance()

        // Declaring the variables
        progressBar = findViewById(R.id.pg)
        percentageTextView = findViewById(R.id.txtProgressBar)

        // Set the maximum value for the progress bar
        progressBar.max = 100

        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference.child("photos")

        // Function to count the number of photos in Firebase Storage
        fun countPhotosInStorage() {
            storageReference.listAll()
                .addOnSuccessListener { listResult ->
                    val photoCount = listResult.items.size
                    println("Number of photos: $photoCount")

                    // Call the function to update the item count and check achievements
                    updateItemCount(photoCount)
                }
                .addOnFailureListener { exception ->
                    println("Error counting photos: $exception")
                }
        }

        // Start updating the progress bar
        updateProgressBar()
        countPhotosInStorage()
    }

    private fun updateAchievement(artCount: Int) {
        // Update the achievement system based on the car count
        if (artCount >= achievementStarter) {
            tvAchievementStarter.text = "Starter (Achieved)"
        } else {
            tvAchievementStarter.text = "Starter"
        }

        if (artCount >= achievementCollector) {
            tvAchievementCollector.text = "Collector (Achieved)"
        } else {
            tvAchievementCollector.text = "Collector"
        }

        if (artCount >= achievementPackrat) {
            tvAchievementPackrat.text = "Packrat (Achieved)"
        } else {
            tvAchievementPackrat.text = "Packrat"
        }
    }

    companion object {
        private const val TAG = "Achievements"
    }
}




// Function to update the item count and check achievements
    private fun updateItemCount(photoCount: Int) {
        itemCount = photoCount

        if (itemCount == 1) {
            unlockAchievement("Starter")
        } else if (itemCount > 2) {
            unlockAchievement("Collector")
        } else if (itemCount == 10) {
            unlockAchievement("Packrat")
        }

        val itemCountText = "Item Count: $itemCount"
        itemCountTextView.text = itemCountText
    }

    private fun unlockAchievement(achievementName: String) {
        if (!achievements[achievementName]!!) {
            achievements[achievementName] = true
            saveAchievementToFirestore(achievementName)
            displayAchievements()
        }
    }

    private fun saveAchievementToFirestore(achievementName: String) {
        val achievementData = hashMapOf(
            "name" to achievementName,
            "unlocked" to true
        )

        // Replace "achievements" with your Firestore collection name
        //firestore.collection("achievements")
         //   .add(achievementData)
          //  .addOnSuccessListener { documentReference ->
            //    println("Achievement saved with ID: ${documentReference.id}")
          //  }
          //  .addOnFailureListener { e ->
          //      println("Error saving achievement: $e")
          //  }
    }

    private fun displayAchievements() {
        println("Your achievements:")
        achievements.forEach { (achievement, unlocked) ->
            val status = if (unlocked) "Unlocked" else "Locked"
            println("$achievement: $status")
        }
    }

    private fun updateProgressBar() {
        val handler = android.os.Handler(android.os.Looper.getMainLooper())
        var progress = 0

        val unlockedAchievementsCount = achievements.count { it.value }
        val totalAchievementsCount = achievements.size
        val progressPercentage = (unlockedAchievementsCount.toFloat() / totalAchievementsCount.toFloat()) * 100

        // Run a background thread to simulate progress updates
        Thread {
            while (progress <= progressPercentage) {
                handler.post {
                    // Update the progress bar and percentage text
                    progressBar.progress = progress
                    percentageTextView.text = "${progress.toInt()}%"
                    //progressBar.secondaryProgress = progressPercentage.toInt()

                    if (progress == 100) {
                        // Progress completed, show a message or perform any necessary action
                    }
                }

                try {
                    Thread.sleep(100) // Simulate progress update delay
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                progress++
            }
        }.start()
    }

