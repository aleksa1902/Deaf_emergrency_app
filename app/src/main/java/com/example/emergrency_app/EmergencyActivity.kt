package com.example.emergrency_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.emergrency_app.police.PoliceActivity

class EmergencyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency)

        val policeButton: ImageButton = findViewById(R.id.policeButton)
        policeButton.setOnClickListener {
            val intent = Intent(this@EmergencyActivity, PoliceActivity::class.java)
            startActivity(intent)
        }

        val ambulanceButton: ImageButton = findViewById(R.id.ambulanceButton)
        ambulanceButton.setOnClickListener {
            // TODO: Logika za poziv hitne pomoći
        }

        val firefightersButton: ImageButton = findViewById(R.id.firefightersButton)
        firefightersButton.setOnClickListener {
            // TODO: Logika za poziv vatrogasaca
        }
    }
}