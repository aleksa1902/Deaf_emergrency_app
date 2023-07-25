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
import com.example.emergrency_app.ambulance.data.InjuriesData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class InjuriesActivity : AppCompatActivity() {

    private lateinit var injuryEditText: EditText
    private lateinit var injuryTypeEditText: EditText
    private lateinit var injurySituationEditText: EditText
    private lateinit var bleedingEditText: EditText
    private lateinit var injuryAbleEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_injuries)

        injuryEditText = findViewById(R.id.injuryEditText)
        injuryTypeEditText = findViewById(R.id.injuryTypeEditText)
        injurySituationEditText = findViewById(R.id.injurySituationEditText)
        bleedingEditText = findViewById(R.id.bleedingEditText)
        injuryAbleEditText = findViewById(R.id.injuryAbleEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val injury = injuryEditText.text.toString()
            val injuryType = injuryTypeEditText.text.toString()
            val injurySituation = injurySituationEditText.text.toString()
            val bleedingEditText = bleedingEditText.text.toString()
            val injuryAble = injuryAbleEditText.text.toString()

            val liveLocation = Location("mock_provider")
            liveLocation.latitude = 37.7749
            liveLocation.longitude = -122.4194

            val injuriesData = InjuriesData(
                injury,
                injuryType,
                injurySituation,
                bleedingEditText,
                injuryAble,
                GeoPoint(liveLocation.latitude, liveLocation.longitude)
            )
            db.collection("injury_accident")
            .add(injuriesData)
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