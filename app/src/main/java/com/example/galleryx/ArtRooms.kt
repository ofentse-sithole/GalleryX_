package com.example.galleryx

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class ArtRooms : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var stylist: EditText
    private lateinit var location: EditText
    private lateinit var category: EditText
    private val categories = mutableListOf<String>()
    private lateinit var categoryList: ListView
    private lateinit var save: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var user: String

    // Inputting Image
    private lateinit var selectImageButton: Button
    private lateinit var uploadButton: Button
    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art_rooms)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        user = firebaseAuth.currentUser?.uid.toString()

        name = findViewById(R.id.edt_art_name)
        stylist = findViewById(R.id.edt_art_stylist)
        location = findViewById(R.id.edt_art_location)
        category = findViewById(R.id.edt_art_category)
        save = findViewById(R.id.btn_art_save)
        categoryList = findViewById(R.id.category_listview)

        // Adding image
        selectImageButton = findViewById(R.id.selectImageButton)
        uploadButton = findViewById(R.id.uploadButton)
        imageView = findViewById(R.id.imageView)

        // Initialize and set the bottom navigation view
        bottomNavView = findViewById(R.id.bottomNavigationView)

        // Set the bottom navigation view to be selected when the user is on the Categories page
        bottomNavView.selectedItemId = R.id.menu_category

        // Set the onNavigationItemSelectedListener for the bottom navigation view
        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // Do nothing because the user is already on the homepage
                    startActivity(Intent(this, ArtCollections::class.java))
                    Toast.makeText(this, "Art Collections selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_category -> {
                    // Start the search activity
                    menuItem.isChecked = true
                    true
                }
                R.id.menu_goal -> {
                    // Start the goal activity
                    startActivity(Intent(this, Goal::class.java))
                    Toast.makeText(this, "Goal selected", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.menu_achievements -> {
                    startActivity(Intent(this, Achievements::class.java))
                    Toast.makeText(this, "Achievements selected", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.menu_profile -> {
                    // Start the profile activity
                    startActivity(Intent(this, Profile::class.java))
                    Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }

        selectImageButton.setOnClickListener {
            openGallery()
        }

        uploadButton.setOnClickListener {
            if (imageUri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                uploadImage(bitmap) { downloadUrl ->
                    // Handle the download URL or error here
                    if (downloadUrl != null) {
                        // Image uploaded successfully, you can use the download URL
                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        // Error uploading image
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }

        save.setOnClickListener {
            val cate = category.text.toString()
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
            categoryList.adapter = adapter
            categories.add(cate)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    private fun uploadImage(bitmap: Bitmap, onComplete: (String?) -> Unit) {
        val storageRef = storage.reference
        val imagesRef = storageRef.child("art_images").child(user).child("${System.currentTimeMillis()}.jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            imagesRef.downloadUrl.addOnSuccessListener { uri ->
                onComplete(uri.toString())
            }.addOnFailureListener { exception ->
                onComplete(null)
                // Handle any errors
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            onComplete(null)
            // Handle any errors
            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_GALLERY = 1
    }
}
