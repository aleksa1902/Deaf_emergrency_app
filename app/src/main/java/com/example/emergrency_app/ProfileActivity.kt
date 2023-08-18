package com.example.emergrency_app

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(uid!!)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val firstName = document.getString("firstName")
                    val lastName = document.getString("lastName")
                    val birthYear = document.getString("dob")
                    val jmbg = document.getString("jmbg")
                    val imageUrl = document.getString("profileImage")

                    val nameTextView: TextView = findViewById(R.id.nameTextView)
                    val birthYearTextView: TextView = findViewById(R.id.birthYearTextView)
                    val jmbgTextView: TextView = findViewById(R.id.jmbgTextView)
                    val profileImageView: ImageView = findViewById(R.id.profileImageView)

                    nameTextView.text = "$firstName $lastName"
                    birthYearTextView.text = "Godina rođenja: $birthYear"
                    jmbgTextView.text = "JMBG: $jmbg"

                    Picasso.get().load(imageUrl).into(profileImageView)
                }
            }
            .addOnFailureListener { exception ->
                // Postupajte sa greškom prilikom dohvatanja podataka
            }
    }
}