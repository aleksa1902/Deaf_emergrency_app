package com.example.emergrency_app.police.form

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.emergrency_app.MainActivity
import com.example.emergrency_app.R
import com.example.emergrency_app.police.data.TheftData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class TheftActivity : AppCompatActivity() {

    private lateinit var timeEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var injuryEditText: EditText
    private lateinit var susSituationEditText: EditText
    private lateinit var suspectEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police_theft)

        timeEditText = findViewById(R.id.timeEditText)
        locationEditText = findViewById(R.id.locationEditText)
        injuryEditText = findViewById(R.id.injuryEditText)
        susSituationEditText = findViewById(R.id.susSituationEditText)
        suspectEditText = findViewById(R.id.suspectEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val time = timeEditText.text.toString()
            val location = locationEditText.text.toString()
            val injury = injuryEditText.text.toString()
            val susSituation = susSituationEditText.text.toString()
            val suspect = suspectEditText.text.toString()

            val liveLocation = Location("mock_provider")
            liveLocation.latitude = 37.7749
            liveLocation.longitude = -122.4194

            val theftData = TheftData(
                time,
                location,
                injury,
                susSituation,
                suspect,
                GeoPoint(liveLocation.latitude, liveLocation.longitude)
            )
            db.collection("thieft_activity")
                .add(theftData)
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