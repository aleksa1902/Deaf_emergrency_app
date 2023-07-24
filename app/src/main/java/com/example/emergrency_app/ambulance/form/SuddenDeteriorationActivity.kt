package com.example.emergrency_app.ambulance.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class SuddenDeteriorationActivity : AppCompatActivity() {

    private lateinit var symptomsEditText: EditText
    private lateinit var factorsEditText: EditText
    private lateinit var situationEditText: EditText
    private lateinit var changesEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_sudden_deterioration)

        symptomsEditText = findViewById(R.id.symptomsEditText)
        factorsEditText = findViewById(R.id.factorsEditText)
        situationEditText = findViewById(R.id.situationEditText)
        changesEditText = findViewById(R.id.changesEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val factors = factorsEditText.text.toString()
            val situation = situationEditText.text.toString()
            val changes = changesEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()
            val symptoms = symptomsEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Faktori: $factors\nDa li osoba dise: $situation\nPromene: $changes\nInformacije: $additionalInfo\nSimptomi: $symptoms"
            Log.d("Informacije", logMessage)
        }
    }
}