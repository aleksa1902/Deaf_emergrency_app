package com.example.emergrency_app

import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class ProfileActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        requestQueue = Volley.newRequestQueue(this)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null).toString()
        val currentUser = sharedPreferences.getString("userId", null).toString()

        val endpointUrl = "https://deaf-emergency-api-7y7tv3tc.ew.gateway.dev/user/$currentUser"

        val request = object : JsonObjectRequest(
            Request.Method.GET, endpointUrl, null,
            Response.Listener { response ->
                val firstName = response.getString("firstName")
                val lastName = response.getString("lastName")
                val birthYear = response.getString("dateOfBirth")
                val jmbg = response.getString("JMBG")
                val base64Image = response.getString("profileImage")

                val nameTextView: TextView = findViewById(R.id.nameTextView)
                val birthYearTextView: TextView = findViewById(R.id.birthYearTextView)
                val jmbgTextView: TextView = findViewById(R.id.jmbgTextView)
                val profileImageView: ImageView = findViewById(R.id.profileImageView)

                nameTextView.text = "$firstName $lastName"
                birthYearTextView.text = "Godina rođenja: $birthYear"
                jmbgTextView.text = "JMBG: $jmbg"

                val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
                val decodedBitmap = android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                profileImageView.setImageBitmap(decodedBitmap)
            },
            Response.ErrorListener {
                Toast.makeText(
                    this@ProfileActivity,
                    "Nesto nije proslo kako treba, pokusajte ponovo.",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            // Override getHeaders() to set the Bearer token
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.putAll(super.getHeaders())
                headers["Authorization"] = "Bearer $token"

                println(token)

                return headers
            }
        }
        requestQueue.add(request)
    }
}