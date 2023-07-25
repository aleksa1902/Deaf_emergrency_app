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
import com.example.emergrency_app.police.data.IllegalData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class IllegalActivity : AppCompatActivity() {

    private lateinit var illegalTypeEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var youEditText: EditText
    private lateinit var suspectEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police_illegal)

        locationEditText = findViewById(R.id.locationEditText)
        illegalTypeEditText = findViewById(R.id.illegalTypeEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        youEditText = findViewById(R.id.youEditText)
        suspectEditText = findViewById(R.id.suspectEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val location = locationEditText.text.toString()
            val illegalType = illegalTypeEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()
            val youEditText = youEditText.text.toString()
            val suspect = suspectEditText.text.toString()

            val liveLocation = Location("mock_provider")
            liveLocation.latitude = 37.7749
            liveLocation.longitude = -122.4194

            val illegalData = IllegalData(
                location,
                illegalType,
                additionalInfo,
                youEditText,
                suspect,
                GeoPoint(liveLocation.latitude, liveLocation.longitude)
            )
            db.collection("illegal_activity")
            .add(illegalData)
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