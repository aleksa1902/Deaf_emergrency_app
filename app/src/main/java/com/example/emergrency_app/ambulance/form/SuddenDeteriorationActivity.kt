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
import com.example.emergrency_app.ambulance.data.SuddenDeteriorationData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class SuddenDeteriorationActivity : AppCompatActivity() {

    private lateinit var symptomsEditText: EditText
    private lateinit var factorsEditText: EditText
    private lateinit var situationEditText: EditText
    private lateinit var changesEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_sudden_deterioration)

        symptomsEditText = findViewById(R.id.symptomsEditText)
        factorsEditText = findViewById(R.id.factorsEditText)
        situationEditText = findViewById(R.id.situationEditText)
        changesEditText = findViewById(R.id.changesEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val factors = factorsEditText.text.toString()
            val situation = situationEditText.text.toString()
            val changes = changesEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()
            val symptoms = symptomsEditText.text.toString()

            val liveLocation = Location("mock_provider")
            liveLocation.latitude = 37.7749
            liveLocation.longitude = -122.4194

            val suddenDeteriorationData = SuddenDeteriorationData(
                symptoms,
                additionalInfo,
                factors,
                situation,
                changes,
                GeoPoint(liveLocation.latitude, liveLocation.longitude)
            )
            db.collection("sudden_deterioration")
            .add(suddenDeteriorationData)
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