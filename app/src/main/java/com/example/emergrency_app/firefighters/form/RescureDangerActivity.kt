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
import com.example.emergrency_app.firefighters.data.RescureDangerData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class RescureDangerActivity : AppCompatActivity() {

    private lateinit var dangerNatureEditText: EditText
    private lateinit var numEditText: EditText
    private lateinit var currLocationEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_rescure_danger)

        dangerNatureEditText = findViewById(R.id.dangerNatureEditText)
        numEditText = findViewById(R.id.numEditText)
        currLocationEditText = findViewById(R.id.currLocationEditText)
        dangerEditText = findViewById(R.id.dangerEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val dangerNature = dangerNatureEditText.text.toString()
            val num = numEditText.text.toString()
            val currLocation = currLocationEditText.text.toString()
            val danger = dangerEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()

            val liveLocation = Location("mock_provider")
            liveLocation.latitude = 37.7749
            liveLocation.longitude = -122.4194

            val rescureDangerData = RescureDangerData(
                dangerNature,
                num,
                currLocation,
                danger,
                additionalInfo,
                GeoPoint(liveLocation.latitude, liveLocation.longitude)
            )
            db.collection("rescure_danger")
                .add(rescureDangerData)
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