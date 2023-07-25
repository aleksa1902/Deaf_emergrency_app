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
import com.example.emergrency_app.ambulance.data.ChildBirthData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class ChildbirthActivity : AppCompatActivity() {

    private lateinit var laboursEditText: EditText
    private lateinit var durationLaboursEditText: EditText
    private lateinit var complicationsEditText: EditText
    private lateinit var waterBreakingEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_childbirth)

        laboursEditText = findViewById(R.id.laboursEditText)
        durationLaboursEditText = findViewById(R.id.durationLaboursEditText)
        complicationsEditText = findViewById(R.id.complicationsEditText)
        waterBreakingEditText = findViewById(R.id.waterBreakingEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val duration = durationLaboursEditText.text.toString()
            val complications = complicationsEditText.text.toString()
            val waterBreaking = waterBreakingEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()
            val labours = laboursEditText.text.toString()

            val liveLocation = Location("mock_provider")
            liveLocation.latitude = 37.7749
            liveLocation.longitude = -122.4194

            val childBirthData = ChildBirthData(
                labours,
                duration,
                complications,
                waterBreaking,
                additionalInfo,
                GeoPoint(liveLocation.latitude, liveLocation.longitude)
            )
            db.collection("child_birth")
            .add(childBirthData)
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