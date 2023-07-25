package com.example.emergrency_app.ambulance.form

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.emergrency_app.MainActivity
import com.example.emergrency_app.R
import com.example.emergrency_app.ambulance.data.MentalCrisesData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class MentalCrisesActivity : AppCompatActivity() {

    private lateinit var symptomsEditText: EditText
    private lateinit var durationEditText: EditText
    private lateinit var historyEditText: EditText
    private lateinit var medicinesEditText: EditText
    private lateinit var mentalSafeEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_mental_crises)

        symptomsEditText = findViewById(R.id.symptomsEditText)
        durationEditText = findViewById(R.id.durationEditText)
        historyEditText = findViewById(R.id.historyEditText)
        medicinesEditText = findViewById(R.id.medicinesEditText)
        mentalSafeEditText = findViewById(R.id.mentalSafeEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val duration = durationEditText.text.toString()
            val history = historyEditText.text.toString()
            val medicine = medicinesEditText.text.toString()
            val safe = mentalSafeEditText.text.toString()
            val symptoms = symptomsEditText.text.toString()

            val liveLocation = Location("mock_provider")
            liveLocation.latitude = 37.7749
            liveLocation.longitude = -122.4194

            val mentalCrisesData = MentalCrisesData(
                symptoms,
                duration,
                history,
                medicine,
                safe,
                GeoPoint(liveLocation.latitude, liveLocation.longitude)
            )
            db.collection("mental_crises")
            .add(mentalCrisesData)
            .addOnSuccessListener {
                Toast.makeText(this, "Podaci su uspešno sačuvani u bazi.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Došlo je do greške prilikom upisivanja podataka: ${error.message}", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}