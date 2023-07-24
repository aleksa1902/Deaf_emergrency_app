package com.example.emergrency_app.ambulance.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class MentalCrisesActivity : AppCompatActivity() {

    private lateinit var symptomsEditText: EditText
    private lateinit var durationEditText: EditText
    private lateinit var historyEditText: EditText
    private lateinit var medicinesEditText: EditText
    private lateinit var mentalSafeEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_mental_crises)

        symptomsEditText = findViewById(R.id.symptomsEditText)
        durationEditText = findViewById(R.id.durationEditText)
        historyEditText = findViewById(R.id.historyEditText)
        medicinesEditText = findViewById(R.id.medicinesEditText)
        mentalSafeEditText = findViewById(R.id.mentalSafeEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val duration = durationEditText.text.toString()
            val history = historyEditText.text.toString()
            val medicine = medicinesEditText.text.toString()
            val safe = mentalSafeEditText.text.toString()
            val symptoms = symptomsEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Koliko dugo: $duration\nDa li je vec dijagnostikovano: $history\nTerapija/lekovi: $medicine\nBezbedno: $safe\nSimptomi: $symptoms"
            Log.d("Informacije", logMessage)
        }
    }
}