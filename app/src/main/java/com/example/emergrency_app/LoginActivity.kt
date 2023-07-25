package com.example.emergrency_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(this@LoginActivity, "Prijava neuspešna.", Toast.LENGTH_SHORT).show()
            }else{
            // firebase authentication
            FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Prijava neuspešna.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}