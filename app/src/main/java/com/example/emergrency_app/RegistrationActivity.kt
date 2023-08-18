package com.example.emergrency_app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class RegistrationActivity : AppCompatActivity() {
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var jmbgEditText: EditText
    private lateinit var registerButton: Button

    private lateinit var selectImageButton: Button
    private lateinit var profileImageView: ImageView
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        dobEditText = findViewById(R.id.dobEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        jmbgEditText = findViewById(R.id.jmbgEditText)
        registerButton = findViewById(R.id.registerButton)
        selectImageButton = findViewById(R.id.selectImageButton)
        profileImageView = findViewById(R.id.profileImageView)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }

        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val dob = dobEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val jmbg = jmbgEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // TODO: Validacija polja pre slanja podataka u Firebase

            registerUser(firstName, lastName, dob, email, jmbg, password)
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                selectedImageUri = data.data
                profileImageView.setImageURI(selectedImageUri)
            }
        }
    }

    private fun registerUser(firstName: String, lastName: String, dob: String, email: String, jmbg: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                val uid = user?.uid ?: ""
                val userData = hashMapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "dob" to dob,
                    "jmbg" to jmbg
                )

                // Upload selected image to Firebase Storage
                if (selectedImageUri != null) {
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("profile_images/$uid")
                    val uploadTask = imageRef.putFile(selectedImageUri!!)

                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        // Get the download URL of the uploaded image
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            userData["profileImage"] = imageUrl

                            // Save user data with the image URL
                            val db = FirebaseFirestore.getInstance()
                            db.collection("users").document(uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    // Successfully saved to Firestore
                                    // You can finish this activity and return to LoginActivity
                                    setResult(RESULT_OK)
                                    finish()
                                    Toast.makeText(this, "Uspešna registracija, sada se prijavite.", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Greška prilikom registracije. Pokušajte ponovo.", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                        .addOnFailureListener {
                            Toast.makeText(this, "Greska prilikom čuvanja slike. Pokušajte ponovo.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Save user data without the image URL
                    val db = FirebaseFirestore.getInstance()
                    db.collection("users").document(uid)
                        .set(userData)
                        .addOnSuccessListener {
                            // Successfully saved to Firestore
                            // You can finish this activity and return to LoginActivity
                            setResult(RESULT_OK)
                            finish()
                            Toast.makeText(this, "Uspešna registracija, sada se prijavite.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Greška prilikom registracije. Pokušajte ponovo.", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Greška prilikom registracije. Pokušajte ponovo.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}