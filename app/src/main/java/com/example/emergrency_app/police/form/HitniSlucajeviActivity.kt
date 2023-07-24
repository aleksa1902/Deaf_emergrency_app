package com.example.emergrency_app.police.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class HitniSlucajeviActivity : AppCompatActivity() {

    private lateinit var locationEditText: EditText
    private lateinit var natureEditText: EditText
    private lateinit var injuryEditText: EditText
    private lateinit var numPeopleEditText: EditText
    private lateinit var suspectEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police_hitni_slucajevi)

        locationEditText = findViewById(R.id.locationEditText)
        natureEditText = findViewById(R.id.natureEditText)
        injuryEditText = findViewById(R.id.injuryEditText)
        numPeopleEditText = findViewById(R.id.numPeopleEditText)
        suspectEditText = findViewById(R.id.suspectEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val location = locationEditText.text.toString()
            val nature = natureEditText.text.toString()
            val injury = injuryEditText.text.toString()
            val numPeople = numPeopleEditText.text.toString()
            val suspect = suspectEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Lokacija: $location\nPriroda: $nature\nOzljede: $injury\nBroj osoba: $numPeople\nOpis počinitelja: $suspect"
            Log.d("Informacije", logMessage)
        }
    }
}