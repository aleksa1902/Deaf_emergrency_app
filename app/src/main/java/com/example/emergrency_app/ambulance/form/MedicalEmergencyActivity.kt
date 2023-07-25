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
import com.example.emergrency_app.ambulance.data.MedicalEmergencyData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class MedicalEmergencyActivity : AppCompatActivity() {

    private lateinit var symptomsEditText: EditText
    private lateinit var consciousEditText: EditText
    private lateinit var breathingEditText: EditText
    private lateinit var painEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_emergency)

        symptomsEditText = findViewById(R.id.symptomsEditText)
        consciousEditText = findViewById(R.id.consciousEditText)
        breathingEditText = findViewById(R.id.breathingEditText)
        painEditText = findViewById(R.id.painEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val conscious = consciousEditText.text.toString()
            val breathing = breathingEditText.text.toString()
            val pain = painEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()
            val symptoms = symptomsEditText.text.toString()

            val liveLocation = Location("mock_provider")
            liveLocation.latitude = 37.7749
            liveLocation.longitude = -122.4194

            val medicalEmergencyData = MedicalEmergencyData(
                symptoms,
                conscious,
                breathing,
                pain,
                additionalInfo,
                GeoPoint(liveLocation.latitude, liveLocation.longitude)
            )
            db.collection("medical_emergency")
            .add(medicalEmergencyData)
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