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
import com.example.emergrency_app.firefighters.data.FloodsData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class FloodsActivity : AppCompatActivity() {

    private lateinit var floodLocationEditText: EditText
    private lateinit var floodNumEditText: EditText
    private lateinit var stuckEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_floods)

        floodLocationEditText = findViewById(R.id.floodLocationEditText)
        floodNumEditText = findViewById(R.id.floodNumEditText)
        stuckEditText = findViewById(R.id.stuckEditText)
        dangerEditText = findViewById(R.id.dangerEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val floodLocation = floodLocationEditText.text.toString()
            val floodNum = floodNumEditText.text.toString()
            val stuck = stuckEditText.text.toString()
            val danger = dangerEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()

            val liveLocation = Location("mock_provider")
            liveLocation.latitude = 37.7749
            liveLocation.longitude = -122.4194

            val floodsData = FloodsData(
                floodLocation,
                floodNum,
                stuck,
                danger,
                additionalInfo,
                GeoPoint(liveLocation.latitude, liveLocation.longitude)
            )
            db.collection("floods")
                .add(floodsData)
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