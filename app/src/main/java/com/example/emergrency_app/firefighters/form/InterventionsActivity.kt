package com.example.emergrency_app.firefighters.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class InterventionsActivity : AppCompatActivity() {

    private lateinit var dangerNatureEditText: EditText
    private lateinit var dangerLocationEditText: EditText
    private lateinit var situationEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_interventions)

        dangerNatureEditText = findViewById(R.id.dangerNatureEditText)
        dangerLocationEditText = findViewById(R.id.dangerLocationEditText)
        situationEditText = findViewById(R.id.situationEditText)
        dangerEditText = findViewById(R.id.dangerEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val dangerNature = dangerNatureEditText.text.toString()
            val dangerLocation = dangerLocationEditText.text.toString()
            val situation = situationEditText.text.toString()
            val danger = dangerEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Priroda opasnosti: $dangerNature\nOpasna situacija: $dangerLocation\nSituacija: $situation\nSirenje opasnost: $danger\nInfo opasni materijali: $additionalInfo"
            Log.d("Informacije", logMessage)
        }
    }
}