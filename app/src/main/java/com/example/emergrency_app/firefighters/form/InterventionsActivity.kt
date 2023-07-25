package com.example.emergrency_app.firefighters.form

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.emergrency_app.MainActivity
import com.example.emergrency_app.R
import com.example.emergrency_app.firefighters.data.InterventionsData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class InterventionsActivity : AppCompatActivity() {

    private lateinit var dangerNatureEditText: EditText
    private lateinit var dangerLocationEditText: EditText
    private lateinit var situationEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_interventions)

        dangerNatureEditText = findViewById(R.id.dangerNatureEditText)
        dangerLocationEditText = findViewById(R.id.dangerLocationEditText)
        situationEditText = findViewById(R.id.situationEditText)
        dangerEditText = findViewById(R.id.dangerEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val dangerNature = dangerNatureEditText.text.toString()
            val dangerLocation = dangerLocationEditText.text.toString()
            val situation = situationEditText.text.toString()
            val danger = dangerEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()

            val liveLocation = Location("mock_provider")
            liveLocation.latitude = 37.7749
            liveLocation.longitude = -122.4194

            val interventionsData = InterventionsData(
                dangerNature,
                dangerLocation,
                situation,
                danger,
                additionalInfo,
                GeoPoint(liveLocation.latitude, liveLocation.longitude)
            )
            db.collection("firefighters_interventions")
                .add(interventionsData)
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