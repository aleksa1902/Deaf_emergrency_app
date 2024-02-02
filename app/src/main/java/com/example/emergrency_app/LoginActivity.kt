package com.example.emergrency_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        requestQueue = Volley.newRequestQueue(this)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(this@LoginActivity, "Prijava neuspešna.", Toast.LENGTH_SHORT).show()
            }else{
                val url = "https://deaf-emergency-api-7y7tv3tc.ew.gateway.dev/login"
                val requestBody = JSONObject()
                requestBody.put("email", username)
                requestBody.put("password", password)

                val request = JsonObjectRequest(Request.Method.POST, url, requestBody,
                    { response ->
                        val token = response.getString("token")
                        val tokenExpires = response.getString("tokenExpires")
                        val userId = response.getString("id")

                        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("token", token)
                        editor.putString("tokenExpires", tokenExpires)
                        editor.putBoolean("isLoggedIn", true)
                        editor.putString("userId", userId)
                        editor.apply()

                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                ) {
                    Toast.makeText(this@LoginActivity, "Prijava neuspešna.", Toast.LENGTH_SHORT)
                        .show()
                }

                requestQueue.add(request)
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}