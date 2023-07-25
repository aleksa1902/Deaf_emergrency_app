package com.example.emergrency_app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var jmbgEditText: EditText
    private lateinit var registerButton: Button

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

                val db = FirebaseFirestore.getInstance()
                db.collection("users").document(uid)
                    .set(userData)
                    .addOnSuccessListener {
                        // Uspešno sačuvano u Firestore
                        // Možete završiti ovu aktivnost i vratiti se na LoginActivity
                        setResult(RESULT_OK)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Greška prilikom registracije. Pokušajte ponovo.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Greška prilikom registracije. Pokušajte ponovo.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}