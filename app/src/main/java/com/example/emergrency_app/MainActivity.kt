package com.example.emergrency_app

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import android.view.Menu
import android.view.MenuItem


class MainActivity : AppCompatActivity() {
    private val REQUEST_LOGIN = 1
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        FirebaseApp.initializeApp(this)

        val emergencyButton: Button = findViewById(R.id.emergencyButton)
        emergencyButton.setOnClickListener {
            val intent = Intent(this@MainActivity, EmergencyActivity::class.java)
            startActivity(intent)
        }

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val savedTokenExpires = sharedPreferences.getString("tokenExpires", null)
        val savedTokenExpiresTimestamp = savedTokenExpires.toString().toLong()
        if (!isLoggedIn || isTokenExpired(savedTokenExpiresTimestamp)) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivityForResult(intent, REQUEST_LOGIN)
        }
    }

    private fun isTokenExpired(tokenExpiresTimestamp: Long): Boolean {
        val currentTimestamp = System.currentTimeMillis() / 1000

        return currentTimestamp > tokenExpiresTimestamp
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                // Poziv za odjavu korisnika
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", false)
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivityForResult(intent, REQUEST_LOGIN)
                true
            }
            R.id.menu_profile -> {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_LOGIN && resultCode == Activity.RESULT_OK) {
            // Prijava uspešna, prikaži početnu stranicu
            // Možete dodati logiku za prikaz početne stranice ovde
        }
    }
}